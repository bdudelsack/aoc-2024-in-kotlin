package utils.tree

class TreeNode<T>(val content: T, private val children: MutableList<TreeNode<T>> = mutableListOf()) {
    fun addChild(node: TreeNode<T>) = children.add(node)
    fun removeChild(node: TreeNode<T>) = children.remove(node)
    fun forEachChild(action: (TreeNode<T>) -> Unit) = children.forEach(action)
    fun forEachChildIndexed(action: (Int, TreeNode<T>) -> Unit) = children.forEachIndexed(action)
}
