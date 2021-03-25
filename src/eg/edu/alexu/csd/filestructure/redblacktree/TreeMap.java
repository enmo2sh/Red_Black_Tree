package eg.edu.alexu.csd.filestructure.redblacktree;

import javax.management.RuntimeErrorException;
import java.util.*;

public class TreeMap<T extends Comparable<T>, V> implements ITreeMap {
    private RedBlackTree tree = new RedBlackTree();

    @Override
    public Map.Entry ceilingEntry(Comparable key) {
        if(key == null)
            throw new RuntimeErrorException(null);
        Object value;
        boolean exist = tree.contains(key);
        if(exist)
            value = tree.search(key);
        else{
            INode greater = leastGreater(key);
            if(greater == null)
                return null;
            else {
                value = greater.getValue();
                key = greater.getKey();
            }
        }
        Map.Entry<T, V> result = new AbstractMap.SimpleEntry<T, V>((T) key, (V) value);
        return result;
    }

    private INode leastGreater(Comparable key){
        INode root = tree.getRoot();
        INode prev = null;
        while (root!=null && !root.isNull()){
            if(key.compareTo(root.getKey())<0) {
                prev = root;
                root = root.getLeftChild();
            }
            else if(root.getRightChild()!=null && !root.getRightChild().isNull() && key.compareTo(root.getRightChild().getKey())<0){
                prev = root.getRightChild();
                root = prev.getLeftChild();
            }
            else
                return prev;
        }
        return prev;
    }

    @Override
    public Comparable ceilingKey(Comparable key) {
        if(key == null)
            throw new RuntimeErrorException(null);
        boolean exist = tree.contains(key);
        if(exist)
            return key;
        INode greater = leastGreater(key);
        if(greater == null)
            return null;
        return greater.getKey();
    }

    @Override
    public void clear() {
        tree.clear();
    }

    @Override
    public boolean containsKey(Comparable key) {
        if(key == null)
            throw new RuntimeErrorException(null);
        return tree.contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        if(value == null)
            throw new RuntimeErrorException(null);
        int[] flag = new int[1];
        flag[0]=0;
        flag = getloop(tree.getRoot(), value, flag);
        if(flag[0]==1)
            return true;
        return false;
    }

    private int[] getloop(INode root, Object value, int[] flag) {
        if (root != null && !root.isNull()) {
            getloop(root.getLeftChild(), value, flag);
            if(root.getValue().equals(value)){
                flag[0]=1;
                return flag;
            }
            getloop(root.getRightChild(), value, flag);
        }
        return flag;
    }

    @Override
    public Set<Map.Entry> entrySet() {
        if(tree.getRoot()==null || tree.getRoot().isNull())
            return null;
        Set<Map.Entry> set = new LinkedHashSet<>();
        set = loop(tree.getRoot(), set);
        return set;
    }

    private Set<Map.Entry> loop(INode root, Set<Map.Entry> set) {
        if (root != null && !root.isNull()) {
            loop(root.getLeftChild(), set);
            Map.Entry<T, V> node = new AbstractMap.SimpleEntry<T, V>((T) root.getKey(), (V) root.getValue());
            set.add(node);
            loop(root.getRightChild(), set);
        }
        return set;
    }

    @Override
    public Map.Entry firstEntry() {
        if(tree.isEmpty())
            return null;
        INode root = tree.getRoot();
        INode prev = tree.getRoot();
        if (tree.isEmpty())
            return null;
        while (root!=null && !root.isNull()){
            prev = root;
            root = root.getLeftChild();
        }
        Map.Entry <T, V> first = new AbstractMap.SimpleEntry<T, V>((T)prev.getKey(), (V) prev.getValue());
        return first;
    }

    @Override
    public Comparable firstKey() {
        if(tree.isEmpty())
            return null;
        INode root = tree.getRoot();
        INode prev = tree.getRoot();
        while (root!=null && !root.isNull()){
            prev = root;
            root = root.getLeftChild();
        }
        return prev.getKey();
    }

    @Override
    public Map.Entry floorEntry(Comparable key) {
        if(key == null)
            throw new RuntimeErrorException(null);
        if(tree.isEmpty())
            return null;
        Object value;
        boolean exist = tree.contains(key);
        if(exist)
            value = tree.search(key);
        else{
            INode less = greatestLower(key);
            if(less == null)
                return null;
            else {
                value = less.getValue();
                key = less.getKey();
            }
        }
        Map.Entry<T, V> result = new AbstractMap.SimpleEntry<T, V>((T) key, (V) value);
        return result;
    }

    private INode greatestLower(Comparable key){
        if(key == null)
            throw new RuntimeErrorException(null);
        INode root = tree.getRoot();
        INode prev = null;
        while (root!=null && !root.isNull()){
            if(key.compareTo(root.getKey())>0) {
                prev = root;
                root = root.getRightChild();
            }
            else if(root.getLeftChild()!=null && !root.getLeftChild().isNull()&& key.compareTo(root.getLeftChild().getKey())>0){
                prev = root.getLeftChild();
                root = prev.getRightChild();
            }
            else
                return prev;
        }
        return prev;
    }

    @Override
    public Comparable floorKey(Comparable key) {
        if(key == null)
            throw new RuntimeErrorException(null);
        if(tree.isEmpty())
            return null;
        boolean exist = tree.contains(key);
        if(exist)
            return key;
        INode less = greatestLower(key);
        if(less == null)
            return null;
        return less.getKey();
    }

    @Override
    public Object get(Comparable key) {
        if(key == null)
            throw new RuntimeErrorException(null);
        Object value = tree.search(key);
        return value;
    }

    @Override
    public ArrayList<Map.Entry> headMap(Comparable toKey) {
        if(toKey == null)
            throw new RuntimeErrorException(null);
        ArrayList<Map.Entry> head = new ArrayList<>();
        head = loophead(toKey, tree.getRoot(),head );
        return head;
    }

    @Override
    public ArrayList<Map.Entry> headMap(Comparable toKey, boolean inclusive) {
        if(toKey == null)
            throw new RuntimeErrorException(null);
        ArrayList<Map.Entry> head = new ArrayList<>();
        head = loophead(toKey, tree.getRoot(),head );
        if(inclusive){
            if(tree.contains(toKey)) {
                Map.Entry<T, V> current = new AbstractMap.SimpleEntry<T, V>((T) toKey, (V) tree.search(toKey));
                head.add(current);
            }
        }
        return head;
    }

    private ArrayList<Map.Entry> loophead(Comparable key, INode root, ArrayList<Map.Entry> head) {
        if (root != null && !root.isNull()) {
            loophead(key, root.getLeftChild(), head);
            if(key.compareTo(root.getKey())>0) {
                Map.Entry<T, V> node = new AbstractMap.SimpleEntry<T, V>((T) root.getKey(), (V) root.getValue());
                head.add(node);
            }
            loophead(key, root.getRightChild(), head);
        }
        return head;
    }

    @Override
    public Set keySet() {
        if(tree.isEmpty())
            return null;
        Set<Comparable> set = new LinkedHashSet<>();
        set = keyloop(tree.getRoot(),set);
        return set;
    }

    private Set keyloop(INode root, Set set) {
        if (root != null && !root.isNull()) {
            keyloop(root.getLeftChild(), set);
            set.add(root.getKey());
            keyloop(root.getRightChild(), set);
        }
        return set;
    }

    @Override
    public Map.Entry lastEntry() {
        if(tree.isEmpty())
            return null;
        INode root = tree.getRoot();
        INode prev = tree.getRoot();
        if (tree.isEmpty())
            return null;
        while (root!=null && !root.isNull()){
            prev = root;
            root = root.getRightChild();
        }
        Map.Entry <T, V> last = new AbstractMap.SimpleEntry<T, V>((T)prev.getKey(), (V) prev.getValue());
        return last;
    }

    @Override
    public Comparable lastKey() {
        if(tree.isEmpty())
            return null;
        INode root = tree.getRoot();
        INode prev = tree.getRoot();
        while (root!=null && !root.isNull()){
            prev = root;
            root = root.getRightChild();
        }
        return prev.getKey();
    }

    @Override
    public Map.Entry pollFirstEntry() {
        if(tree.isEmpty())
            return null;
        Map.Entry <T, V> first = firstEntry();
        tree.delete(first.getKey());
        return first;
    }

    @Override
    public Map.Entry pollLastEntry() {
        if(tree.isEmpty())
            return null;
        Map.Entry <T, V> last = lastEntry();
        tree.delete(last.getKey());
        return last;
    }

    @Override
    public void put(Comparable key, Object value) {
        if(key == null || value==null)
            throw new RuntimeErrorException(null);
        tree.insert(key, value);
    }

    @Override
    public void putAll(Map map) {
        if(map == null)
            throw new RuntimeErrorException(null);
        Iterator<Map.Entry> itr = map.entrySet().iterator();
        while(itr.hasNext()) {
            Map.Entry entry = itr.next();
            put((Comparable) entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean remove(Comparable key) {
        if(key == null)
            throw new RuntimeErrorException(null);
        boolean exist = tree.contains(key);
        if(exist)
            tree.delete(key);
        return exist;
    }

    @Override
    public int size() {
        int[] s = new int[1];
        s[0] = 0;
        if(!tree.isEmpty())
            sizeloop(tree.getRoot(),s);
        return s[0];
    }

    private int[] sizeloop(INode root, int[] s) {
        if (root != null && !root.isNull()) {
            sizeloop(root.getLeftChild(), s);
            s[0]++;
            sizeloop(root.getRightChild(), s);
        }
        return s;
    }

    @Override
    public Collection values() {
        if(tree.isEmpty())
            return null;
        Collection <Object> value = new LinkedList<Object>();
        value = valueloop(tree.getRoot(), value);
        return value;
    }

    private Collection valueloop(INode root, Collection value) {
        if (root != null && !root.isNull()) {
            valueloop(root.getLeftChild(), value);
            value.add(root.getValue());
            valueloop(root.getRightChild(), value);
        }
        return value;
    }
}
