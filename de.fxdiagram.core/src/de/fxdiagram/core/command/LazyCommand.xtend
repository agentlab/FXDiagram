package de.fxdiagram.core.command

/**
 * Postpones the delegate command creation until it is executed.
 */
abstract class LazyCommand implements AnimationCommand {
	
	AbstractAnimationCommand delegate
	
	protected abstract def AbstractAnimationCommand createDelegate()

	override clearRedoStackOnExecute() {
		delegate?.clearRedoStackOnExecute
	}
	
	override getExecuteAnimation(CommandContext context) {
		delegate = createDelegate
		delegate.getExecuteAnimation(context)
	}
	
	override getUndoAnimation(CommandContext context) {
		delegate.getUndoAnimation(context)
	}
	
	override getRedoAnimation(CommandContext context) {
		delegate.getRedoAnimation(context)
	}
	
}