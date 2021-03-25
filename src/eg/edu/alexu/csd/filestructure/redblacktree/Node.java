package eg.edu.alexu.csd.filestructure.redblacktree;

public class Node <T extends Comparable<T>, V> implements INode{
    private Comparable key;
    private Object value;
    private INode parent;
    private INode leftChild ;
    private INode rightChild ;
    private char cl;

    Node() {
        key = null;
        value = null;
        leftChild = null;
        rightChild = null;
        parent = null;
        cl = 'b';
    }

    @Override
    public void setParent(INode parent) {
        this.parent = parent;
    }

    @Override
    public INode getParent() {
        return parent;
    }

    @Override
    public void setLeftChild(INode leftChild) {
        this.leftChild = leftChild;
    }

    @Override
    public INode getLeftChild() {
        return leftChild;
    }

    @Override
    public void setRightChild(INode rightChild) {
        this.rightChild = rightChild;
    }

    @Override
    public INode getRightChild() {
        return rightChild;
    }

    @Override
    public Comparable getKey() {
        return key;
    }

    @Override
    public void setKey(Comparable key) {
        this.key =  key;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public boolean getColor() {
        if(cl=='r')
            return INode.RED;
        else
            return INode.BLACK;
    }

    @Override
    public void setColor(boolean color) {
        if(color==true)
            cl='r';
        else
            cl='b';
    }

    @Override
    public boolean isNull() {
        if( key == null || value == null || this == null)
            return true;
        else
            return false;
    }

}
