/*
 * Copyright (C) 2015 Thiago Rabelo.
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

package commenttemplate.util.timer;

import commenttemplate.util.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

/**
 *
 * @author thiago
 */
public class TimeIt implements Iterable<Long> {
	
	private static final int DEFAULT_TIMES = 1000;
	
	private int times = DEFAULT_TIMES;
	private ArrayList<Long> deltas;
	private Timing timing;
	private long total = -1;
	private long min = -1;
	private long max = -1;
	private double average = -1.0d;

	public TimeIt(Timing timing) {
		this.timing = timing;
	}

	public TimeIt(Timing timing, int times) {
		this(timing);
		setTimes(times);
	}

	public final TimeIt setTimes(int times) {
		this.times = times;
		deltas = new ArrayList<>(times);
		return this;
	}
	
	private void check() {
		if (deltas == null) {
			deltas = new ArrayList<>(times);
		}
	}
	
	public void start() throws Exception {
		check();
		
		int _total = times;
		long before, after;
		Timing t = timing;
		int i = 0;
		
		try {
			for (; i < _total; i++) {
				before = System.nanoTime();
				t.run(i);
				after = System.nanoTime();
				deltas.add(after - before);
			}
		} catch (Exception ex) {
			throw new Exception(Utils.concat("Executed ", i, " from ", _total, " times."), ex);
		}
	}

	public double nanoToMilis(long nano) {
		return ((double)nano)/1000000.0d;
	}

	public double nanoToMilis(double nano) {
		return nano/1000000.0d;
	}
	
	public long totalNano() {
		if (total >= 0) {
			return total;
		}

		return total = deltas.stream().reduce((a, b) -> a + b).get();
	}
	
	public double totalMilis() {
		return nanoToMilis(totalNano());
	}
	
	public double averageNano() {
		if (average >= 0) {
			return average;
		}
		
		long sum = totalNano();

		return (average = ((double)sum)/(double)times);
	}
	
	public double averageMilis() {
		return nanoToMilis(averageNano());
	}
	
	public long maxNano() {
		if (max >= 0) {
			return max;
		}
		return max = Collections.max(deltas);
	}
	
	public long minNano() {
		if (min >= 0) {
			return min;
		}
		return min = Collections.min(deltas);
	}
	
	public double maxMilis() {
		if (max >= 0) {
			return nanoToMilis(max);
		}
		return nanoToMilis(maxNano());
	}
	
	public double minMilis() {
		if (min >= 0) {
			return nanoToMilis(min);
		}
		return nanoToMilis(minNano());
	}
	
	public int getTimes() {
		return times;
	}

	@Override
	public Iterator<Long> iterator() {
		check();
		return deltas.iterator();
	}
}
