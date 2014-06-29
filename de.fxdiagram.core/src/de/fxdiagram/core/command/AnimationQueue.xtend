package de.fxdiagram.core.command

import java.util.List
import java.util.Queue
import javafx.animation.Animation
import javafx.animation.SequentialTransition
import com.google.common.collect.Lists

import static extension de.fxdiagram.annotations.ForeachExtensions.*

class AnimationQueue {
	
	Queue<()=>Animation> queue = newLinkedList
	
	List<Listener> listeners = newArrayList
	
	def void addListener(Listener listener) {
		listeners.add(listener)
	}
	
	def void removeListener(Listener listener) {
		listeners.remove(listener)
	}
	
	def enqueue(()=>Animation animationFactory) {
		if(animationFactory != null) {
			synchronized(queue) {
				val isEmpty = queue.empty
				queue.add(animationFactory)
				if(isEmpty) 
					executeNext
			}
		}
	}

	protected def void executeNext() {
		val next = synchronized(queue) {
			queue.peek
		}
		if(next != null) {
			val animation = next.apply
			if(animation != null) {
				new SequentialTransition => [
					children += animation
					onFinished = [
						synchronized(queue) queue.poll
						executeNext
					]
					play
				]
			} else {
				synchronized(queue) queue.poll
				executeNext
			} 
		} else {
			Lists.newArrayList(listeners).forEachExt[handleQueueEmpty]
		}
	}
	
	interface Listener {
		def void handleQueueEmpty()
	}
}
