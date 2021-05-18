import java.util.*;

public class bst extends company{
    private static class nodes{
        String name;
        Node nod;
        nodes parent;
        nodes leftchild;
        nodes rightchild;
    }
    private static nodes root = new nodes();
    private static boolean isgreater(String k, String l){
        int n = Math.min(k.length(), l.length());
        for(int i=0;i<n;i++){
            if(k.charAt(i)>l.charAt(i)) return true;
            else if(k.charAt(i)<l.charAt(i)) return false;
        }
        if(k.length()>l.length()) return true;
        else return false;
    }
    public void put(String m, Node born){
        nodes re = root;
        nodes pa = new nodes();
        while(re.name!=null){
            pa = re; 
            if(isgreater(m,re.name)) re= re.rightchild;
            else re=re.leftchild;
            re.parent = pa;
        }
        re.name = m;
        re.nod = born;
        re.rightchild = new nodes();
        re.leftchild = new nodes();
    }
    public Node get(String o) {
        nodes search = root;
        while(search.name!=null){
            if(o.equals(search.name)) return search.nod;
            else if(isgreater(o,search.name))    search= search.rightchild;
            else    search=search.leftchild;
        }
        return null;
    }
    private nodes gets(String n) {
        nodes find = root;
        while(find.name!=null){
            if(n.equals(find.name)) return find;
            else if(isgreater(n,find.name)) find = find.rightchild;
            else find = find.leftchild;
        }
        return null;
    }
    public void remove(String p) {
        nodes g = gets(p);              //if deleting root
        if(g==null){                    // update root if deleting root
            System.out.println("rem excep");
            System.exit(0);
        }
        nodes u = g.parent;
        if(g.leftchild.name==null){
            g.rightchild.parent = u;  
            if(u!=null){
                if(Objects.equals(g, u.leftchild)) u.leftchild = g.rightchild;
                else u.rightchild = g.rightchild;
            }
            else root = g.rightchild;
            g.leftchild = null;
            g = null;
        }
        else if(g.rightchild.name==null){
            g.leftchild.parent =u;
            if(u!=null){
                if(Objects.equals(g, u.leftchild)) u.leftchild = g.leftchild;
                else u.rightchild = g.leftchild;
            }
            else root = g.leftchild;
            g.rightchild = null;
            g = null;
        }
        else{
            nodes f = g.rightchild;
            while(f.leftchild.name!=null) f= f.leftchild;
            String q = f.name;
            Node tempnode = f.nod;
            remove(f.name);
            g.name = q;
            g.nod = tempnode;
        }
    }
    public static void main(String[] args) {
        
    }
}