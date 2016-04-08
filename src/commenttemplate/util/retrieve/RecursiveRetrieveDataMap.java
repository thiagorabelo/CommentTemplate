/*
 * Copyright (C) 2015 thiago.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package commenttemplate.util.retrieve;

import commenttemplate.util.Join;
import commenttemplate.util.Utils;
import java.util.Map;

/**
 *
 * @author thiago
 */
// @TODO: Reescrever todo este código.
// Ideia: Em "a.b.c.d". Tentar na ordem:
//     "a".getB().getC().getD()
//     "a.b.c.d"
//     "a.b.c".getD()
//     "a.b".getC().getD()
public class RecursiveRetrieveDataMap implements RetrieveData<Map<String, Object>> {

	//Entrada é VAR_PATH, saída é o valor encontrado. Vazio é retornado se nada for encontrado.
	@Override
	public Object getValue(Map<String,Object> mapValues, String ...keys) {
		return getValue(mapValues, "", Join.with(".").these(keys).toString());
	}

	/**
	 * Busca recursivamente o atributo
	 * a.b.c.d por exemplo...
	 * procura para ver se tem no mapa a.b.c.d, se tiver retorna, senão
	 * procura a.b.c no mapa, se tiver, retorna obj.getD(), senão...
	 * procura a.b no mapa , se tiver, retorna obj.getC().getD(), senão ...
	 * procura a no mapa , se tiver, retorna obj.getB().getC().getD(), senão ...
	 * senão retorna Texto VAZIO.
	 * 
	 * @param key
	 * @param fieldPath
	 * @param mapValues
	 * @return 
	 */
	public static Object getValue(Map<String, Object> mapValues, String fieldPath, String key) {
		Object value = null;

		if (mapValues.get(key) != null) {
			if(!Utils.empty(fieldPath)) {
				value = Utils.getProperty(mapValues.get(key), fieldPath);
			} else {
				value = mapValues.get(key);
			}
		} else if (key.contains(".")) {
			int lastPoint = key.lastIndexOf(".");
			value = getValue(
				mapValues,
				key.substring(0,lastPoint),
				Join.with(".").skipNulls().these(
					key.substring(lastPoint + 1, key.length()),
					Utils.empty(fieldPath) ? fieldPath : null
				).s()
			);
		}

		return value;
	}
};
