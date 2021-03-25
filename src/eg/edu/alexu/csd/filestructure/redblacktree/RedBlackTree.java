package eg.edu.alexu.csd.filestructure.redblacktree;

import javax.management.RuntimeErrorException;

public class RedBlackTree<T extends Comparable<T>, V> implements IRedBlackTree {
    private INode BasicRoot = new Node();
    private Comparable lastkey = null;
    int count = 1;


    @Override
    public INode getRoot() {
        return BasicRoot;
    }

    @Override
    public boolean isEmpty() {
        if (BasicRoot==null || BasicRoot.isNull())
            return true;
        else
            return false;
    }

    @Override
    public void clear() {
        BasicRoot.setKey(null);
    }

    @Override
    public Object search(Comparable key) {
        INode root = getRoot();
        if (key == null)
            throw new RuntimeErrorException(null);
        while (root != null && !root.isNull()) {
            int comp = key.compareTo(root.getKey());
            if (comp == 0)
                return root.getValue();
            else if (comp < 0)
                root = root.getLeftChild();
            else if (comp > 0)
                root = root.getRightChild();
        }
        return null;
    }

    @Override
    public boolean contains(Comparable key) {
        INode root = getRoot();
        if (key == null)
            throw new RuntimeErrorException(null);
        while (root != null && !root.isNull()) {
            int comp = key.compareTo(root.getKey());
            if (comp == 0)
                return true;
            else if (comp < 0)
                root = root.getLeftChild();
            else if (comp > 0)
                root = root.getRightChild();
        }
        return false;
    }

    @Override
    public void insert(Comparable key, Object value) {
        if(key==null || value==null)
            throw new RuntimeErrorException(null);
        INode available = getByKey(key);
        if (available != null && !available.isNull())
            available.setValue(value);
        else {
            BSinsert(getRoot(), key, value, new Node());
            INode last = getByKey(lastkey);
            count++;
            Coloring(last);
        }
        return;
    }

    private INode getByKey(Comparable key) {
        INode root = getRoot();
        while (root != null && !root.isNull()) {
            int comp = key.compareTo(root.getKey());
            if (comp == 0)
                return root;
            else if (comp < 0)
                root = root.getLeftChild();
            else if (comp > 0)
                root = root.getRightChild();
        }
        return null;
    }

   private INode BSinsert(INode root, Comparable key, Object value, INode parent) {
        if (root == null || root.isNull() ) {
            if(root == null)
                root = new Node();

            if (getRoot() == null || getRoot().isNull()) {
                root.setColor(false);
                BasicRoot = root;
            }
            else
                root.setColor(true);

            root.setKey(key);
            root.setValue(value);
            root.setParent(parent);
            lastkey = root.getKey();
            INode l = new Node();
            l.setKey(null);
            root.setLeftChild(l);
            INode r = new Node();
            r.setKey(null);
            root.setRightChild(r);
            return root;
        }

        if (key.compareTo(root.getKey()) < 1) {
            root.setLeftChild(BSinsert(root.getLeftChild(), key, value, root));
        } else if (key.compareTo(root.getKey()) == 1) {
            root.setRightChild(BSinsert(root.getRightChild(), key, value, root));
        }
        return root;
    }

    private void Coloring(INode node) {
        INode x = node;
        INode p = null;
        INode g = null;
        INode u = null;
        INode t = null;
        if (x!=null && !x.isNull() && x.getColor() == true) {
            p = x.getParent();
            if (!p.isNull() && !p.isNull()) {
                g = p.getParent();
                if (!g.isNull() && !g.isNull()) {
                    if (g.getLeftChild() != null && !g.getLeftChild().isNull() && p.getKey().compareTo(g.getLeftChild().getKey()) == 0)
                        u = g.getRightChild();
                    else
                        u = g.getLeftChild();
                    if (u != null && !u.isNull() && u.getColor() == true && p.getColor() == true)  {
                        p.setColor(false);
                        u.setColor(false);
                        g.setColor(true);
                        Coloring(g);
                    }
                    else if(p.getColor() == true) {
                        INode New = UncleBlack(x, p, g, u);
                        Coloring(New);
                    }
                }
            } else
                x.setColor(false);
        }
        return;
    }

    private INode UncleBlack(INode x, INode p, INode g, INode u) {
        if (g.getLeftChild() != null && !g.getLeftChild().isNull() && p.getKey().compareTo(g.getLeftChild().getKey()) == 0) {
            if (p.getLeftChild() != null && !p.getLeftChild().isNull() && x.getKey().compareTo(p.getLeftChild().getKey()) == 0) {
                //LeftLeft
                RightRotation(g);
                boolean temp = p.getColor();
                p.setColor(g.getColor());
                g.setColor(temp);
                return p;
            } else {
                //LeftRight
                LeftRotation(p);
                RightRotation(g);
                boolean temp = x.getColor();
                x.setColor(g.getColor());
                g.setColor(temp);
                return x;
            }
        } else {
            if (p.getLeftChild() != null && !p.getLeftChild().isNull() && x.getKey().compareTo(p.getLeftChild().getKey()) == 0) {
                //RightLeft
                RightRotation(p);
                LeftRotation(g);
                boolean temp = x.getColor();
                x.setColor(g.getColor());
                g.setColor(temp);
                return x;
            } else {
                //RightRight
                LeftRotation(g);
                boolean temp = p.getColor();
                p.setColor(g.getColor());
                g.setColor(temp);
                return p;
            }
        }
    }

    private void LeftRotation(INode a) {
        INode grand = a.getParent();
        INode b = a.getRightChild();
        INode c = b.getLeftChild();
        // Perform rotation
        b.setLeftChild(a);
        a.setRightChild(c);
        if (grand==null || grand.isNull())
            BasicRoot = b;
        else {
            if (grand.getLeftChild() != null && !grand.getLeftChild().isNull() && a.getKey().compareTo(grand.getLeftChild().getKey()) == 0)
                grand.setLeftChild(b);
            else
                grand.setRightChild(b);
        }
        a.setParent(b);
        if (c != null && !c.isNull())
            c.setParent(a);
        b.setParent(grand);
        return;
    }

    private void RightRotation(INode a) {
        INode b = a.getLeftChild();
        INode c = b.getRightChild();
        // Perform rotation
        b.setRightChild(a);
        a.setLeftChild(c);
        INode grand = a.getParent();
        if (grand == null || grand.isNull())
            BasicRoot = b;
        else {
            if ( grand.getLeftChild() != null && !grand.getLeftChild().isNull() && a.getKey().compareTo(grand.getLeftChild().getKey()) == 0)
                grand.setLeftChild(b);
            else
                grand.setRightChild(b);
        }
        a.setParent(b);
        if (c != null && !c.isNull())
            c.setParent(a);
        b.setParent(grand);
        return;
    }

    @Override
    public boolean delete(Comparable key) {
        if(key==null)
            throw new RuntimeErrorException(null);
        boolean result=Delete(getRoot(),key);
        return result;
    }

    private boolean Delete(INode root,Comparable key){
        if( root.isNull())
            return false;
        else if(key.compareTo(root.getKey())<0)
            Delete(root.getLeftChild(),key);
        else if(key.compareTo(root.getKey())>0)
            Delete(root.getRightChild(),key);
        else  {
            //case1 no children
            if((root.getLeftChild().isNull())&&(root.getRightChild().isNull())){
                if(root==getRoot()) {
                    BasicRoot.setKey(null);
                    removeNode(root);
                }
                else {
                    if (isBlackNode(root))
                        DoubleBlack(root);
                    removeNode(root);
                }
            }
            //case2 one child
            else if(root.getLeftChild().isNull()){
                INode temp=root.getRightChild();
                root.setKey(temp.getKey());
                root.setValue(temp.getValue());
                Delete(temp,temp.getKey());

            }
            else if(root.getRightChild().isNull()){
                INode temp=root.getLeftChild();
                root.setKey(temp.getKey());
                root.setValue(temp.getValue());
                Delete(temp,temp.getKey());
            }
            else{
                INode temp=FindMin(root.getRightChild());
                root.setKey(temp.getKey());
                root.setValue(temp.getValue());
                Delete(root.getRightChild(),temp.getKey());
            }
        }
        return true;
    }

    private void removeNode(INode node){
        node.setKey(null);
        node.setValue(null);
        node.setColor(false);
        node.setRightChild(null);
        node.setLeftChild(null);
    }

    private INode FindMin(INode r){
        INode min=r;
        r=r.getLeftChild();
        while(!r.isNull()){
            if(r.getKey().compareTo(min.getKey())<0)
                min=r;
            r=r.getLeftChild();
        }
        return min;
    }

    private boolean isLeftChild(INode r){
        if(r.getParent().getLeftChild()==r)
            return true;
        else
            return false;
    }

    private void DoubleBlack(INode DB){
        //case2
        if(DB==getRoot())
            return;

            //case3
        else if((Slibbing(DB).isNull())||((isBlackNode(Slibbing(DB)))&&(isBlackNode(Slibbing(DB).getRightChild()))&&(isBlackNode(Slibbing(DB).getLeftChild())))){
            //System.out.println("Case3");
            if(!Slibbing(DB).isNull())
                Slibbing(DB).setColor(true);
            if(isBlackNode(DB.getParent()))
                DoubleBlack(DB.getParent());
            else
                DB.getParent().setColor(false);
        }
        //case4
        else if(!isBlackNode(Slibbing(DB))){
            //System.out.println("Case4");
            SwapColors(Slibbing(DB),DB.getParent());
            if(isLeftChild(DB))
                LeftRotation(DB.getParent());
            else
                RightRotation(DB.getParent());
            DoubleBlack(DB);
        }
        //case5
        else if((isBlackNode(Slibbing(DB)))&&(isBlackNode(farDB(DB)))&&(!isBlackNode(nearDB(DB)))){
            //System.out.println("Case5");
            SwapColors(Slibbing(DB),nearDB(DB));
            if(isLeftChild(DB))
                RightRotation(Slibbing(DB));
            else
                LeftRotation(Slibbing(DB));
            Case6(DB);
        }
        else
            Case6(DB);

    }

    private void Case6(INode DB){
        //System.out.println("Case6");
        if(isBlackNode(Slibbing(DB))&&(!isLeftChild(DB))&&(!isBlackNode(Slibbing(DB).getLeftChild()))){
            SwapColors(DB.getParent(),Slibbing(DB));
            Slibbing(DB).getLeftChild().setColor(false);
            if(isLeftChild(DB))
                LeftRotation(DB.getParent());
            else
                RightRotation(DB.getParent());
        }
        else if(isBlackNode(Slibbing(DB))&&(isLeftChild(DB))&&(!isBlackNode(Slibbing(DB).getRightChild()))){
            SwapColors(DB.getParent(),Slibbing(DB));
            Slibbing(DB).getRightChild().setColor(false);
            if(isLeftChild(DB))
                LeftRotation(DB.getParent());
            else
                RightRotation(DB.getParent());
        }
        else
            return;
    }

    private boolean isBlackNode(INode node){
        if(node.isNull())
            return true;
        else if(!node.getColor())
            return true;
        else
            return false;
    }

    private INode nearDB(INode DB){
        INode near;
        if(isLeftChild(DB))
            near=Slibbing(DB).getLeftChild();
        else
            near=Slibbing(DB).getRightChild();
        return near;
    }

    private INode farDB(INode DB){
        INode far;
        if(isLeftChild(DB))
            far=Slibbing(DB).getRightChild();
        else
            far=Slibbing(DB).getLeftChild();
        return far;
    }

    private INode Slibbing(INode DB){
        INode slib;
        if(DB.getParent().isNull())
            return null;
        if(isLeftChild(DB))
            slib=DB.getParent().getRightChild();
        else
            slib=DB.getParent().getLeftChild();
        return slib;
    }

    private void SwapColors(INode node1,INode node2){
        boolean temp=node1.getColor();
        node1.setColor(node2.getColor());
        node2.setColor(temp);
    }

}