---
title: Comp2240 - Assignment 1
author: Cody Lewis [c3283349@uon.edu.au](mailto:c3283349@uon.edu.au)
date: \today
geometry: margin=2cm
linkcolor: blue
---

# Datafile 1

The Narrow Round Robin scheduling algorithm was the best performing for the
first data file, and First Come First Served performed the worst. Both Round
Robin and Feedback performed equally in this case. The performance of these
algorithms is maximised by the reduction of time spent of processes for longer
amounts of times spent on processes in the case where all the processes arrive
at the same time. This may be observed with First Come First Served perfoming
the worst, since it sequentially processes each whole process. Next, Round
Robin and Feedback which perform equally as they both have a constant slice
size, yet they perform better than FCFS as involve more switching between
processes which allows smaller processes to wait less before completing.
Finally Narrow Round Robin is the best performing as it reduces the slice size
for each time it processes a process, this makes it so that less expensive
processes have to wait for less time before their turnaround.

# Datafile 2

In this data file, the processes each come in a differing times, this made the
scheduling algorithms in order from worst performing to best performing
Narrow Round Robin, Round Robin, First Come First Served, and Feedback. Both
Round Robins performed badly in this case as they switch through processes on
a queue structure with no priority system, this means the that the first
process will process for a slice, other processes get added in, then when the
slice is finished, the first will have to wait through the slices of the added
processes before continuing. Narrowing makes this worse by making so that first
process has even less processing time before switching. First Come First Served
performs a bit better as it completes each whole process when it is dequeued.
Feedback Perfroms the best in this case due to the priority system, this means
that even though there are slices, each process is placed on a tier that
marks the immediacy of the need to process them, thus making for less waiting
times.

# General Observation

From these observations, it can be concluded that the Feedback scheduler is on
average the best performing of the scheduling algorithms, and that whether the
Round Robin schedulers or the First Come First Served schedulers perform better
then one another is dependant on the timing of how the processes arrive.

The Feedback scheduler performs best on average due to its combination of
slicing processing and the priority system. The slicing of processing makes it
so that a process does not take up the processor for too long of periods of
time, and thus allows shorter processes to complete without having to wait too
long for those longer ones. The priority system makes it so that the a service
does not have to wait to long to start processing, this allows shorter
processes to be finished quite quickly, while longer ones may have spend a bit
of time waiting, although not too long as the shorter processes will be
finished and eliminated from the queue quite quickly.

The First Come First Served scheduler performed better when processes arrive at
different times as it processes those sequentially in order of arrival, this
reduces waiting time in a lot of cases.

The Round Robin schedulers performed best when all of the processes arrived
at the same time. This is due to the slicing of processing, which means that
smaller processes do not have to wait through some of the bigger ones to
complete processing before having a chance to process. The narrowing variation
of the scheduler tended to perform better as it tended to give the processes
more of a chance to do some processing in a similar way to that of priority
system.
