import java.util.*;
import java.io.*;
import java.security.SecureRandom;


public class Tree<E>{

    class TreeNode<E>{
        E element;
        List<TreeNode<E>> children;
        TreeNode<E> parent;
        int level = 0;

        public TreeNode(E element){
            this.element = element;
            this.level = 0;
            this.children = new LinkedList<TreeNode<E>>();
            this.parent = null;
        }

        public void insert_child(TreeNode<E> child){
            children.add(child);
            child.parent = this;
            child.level = this.level + 1;
        }

        public void delete_child(TreeNode<E> child){
            int index_of_child = children.indexOf(child);
            children.remove(index_of_child);
            child.parent = null;
            child.level = -1;
        }

        public List<TreeNode<E>> get_ancestors(){
            TreeNode<E> current = this.parent;
            List<TreeNode<E>> ancestors_list = new LinkedList<TreeNode<E>>();
            while(current != null){
                ancestors_list.add(0,current);
                current = current.parent;
            }
            return ancestors_list;
        }
    }

    TreeNode<E> root_node;
    HashMap<E,TreeNode<E>> map;

    public Tree(E root){
        root_node = new TreeNode<E>(root);
        map = new HashMap<E,TreeNode<E>>();
        map.put(root, root_node);
    }

    public E get_root(){
        return root_node.element;
    }

    public void insert(E child, E parent){
        TreeNode<E> parent_node = map.get(parent);
        TreeNode<E> child_node = new TreeNode<E>(child);
        parent_node.insert_child(child_node);
        map.put(child, child_node);
    }

    public void move(E element, E new_parent){
        TreeNode<E> element_node = map.get(element);
        TreeNode<E> current_parent_node = map.get(element_node.parent.element);
        TreeNode<E> new_parent_node = map.get(new_parent);
        current_parent_node.delete_child(element_node);
        new_parent_node.insert_child(element_node);
    }

    public int get_level(E element){
        TreeNode<E> element_node = map.get(element);
        return element_node.level;
    }

    public void delete_node(E to_be_deleted, E to_be_shifted){
        TreeNode<E> to_be_deleted_node = map.get(to_be_deleted);
        TreeNode<E> to_be_shifted_node = map.get(to_be_shifted);
        for(TreeNode<E> child_node : to_be_deleted_node.children){
            to_be_shifted_node.insert_child(child_node);
        }
        TreeNode<E> parent_of_to_be_deleted_node = to_be_deleted_node.parent;
        parent_of_to_be_deleted_node.delete_child(to_be_deleted_node);
    }

    public E lowest_common_boss(E element1, E element2){
        TreeNode<E> element1_node = map.get(element1);
        TreeNode<E> element2_node = map.get(element2);
        List<TreeNode<E>> ancestors1 = element1_node.get_ancestors();
        List<TreeNode<E>> ancestors2 = element2_node.get_ancestors();
        TreeNode<E> mostRecentAncestor = null;
        Iterator<TreeNode<E>> it1 = ancestors1.iterator();
        Iterator<TreeNode<E>> it2 = ancestors2.iterator();
        while(it1.hasNext() && it2.hasNext()){
            TreeNode<E> temp1 = it1.next();
            TreeNode<E> temp2 = it2.next();
            if(temp1 == temp2){
                mostRecentAncestor = temp1;
                continue;
            }
            break;
        }
        return mostRecentAncestor.element;
    }

    public void level_map_build(TreeNode<E> node, HashMap<E,Integer> level_map, Integer level){
        level_map.put(node.element, level);
        for(int  i=0; i<node.children.size(); i++){
            level_map_build(node.children.get(i), level_map, (level+1));
        }   
        return;
    }

    static class RandomString {
        private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private static final SecureRandom RANDOM = new SecureRandom();
    
        /**
         * Generates random string of given length from Base65 alphabet (numbers, lowercase letters, uppercase letters).
         *
         * @param count length
         * @return random string of given length
         */
        public static String generate(int count) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < count; ++i) {
                sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
            }
            return sb.toString();
        }
    }

    public static Tree<String> generate_random_tree(int levels, Vector<Vector<String>> BFS, Vector<String> TreeGen, Vector<String> AllEmployees, HashMap<String, Integer> indexInAllEmployees){
        Tree<String> randomTree = new Tree<String>("CEO");
        Vector<String> previous_level = new Vector<String>();
        previous_level.add("CEO");
        Random rand = new Random();
        boolean flag = true;
        BFS.add(previous_level);
        int count = 0;
        for(int level = 1; level < levels; level++){
            int parent_idx = 0;
            Vector<String> current_level = new Vector<String>();
            for(String parent : previous_level){
                int num_children = rand.nextInt(5);
                if(flag && num_children == 0){
                    while(num_children == 0){
                        num_children = rand.nextInt(5);
                    }
                }
                flag = false;
                if(num_children==0){
                    num_children = rand.nextInt(5);
                }
                for(int child_idx = 0; child_idx < num_children; child_idx++){
                    String name = RandomString.generate(7) + "_" + Integer.toString(level) + "_" + Integer.toString(parent_idx) + "_" + Integer.toString(child_idx);
                    current_level.add(name);
                    AllEmployees.add(name);
                    randomTree.insert(name,parent);
                    TreeGen.add(name+" "+parent);
                    indexInAllEmployees.put(name, count);
                    count++;
                }
                parent_idx++;
            }
            BFS.add(current_level);
            previous_level = current_level;
        }
        return randomTree;
    }
    public static void main(String[] args){
        int input_height = Integer.parseInt(args[0]);          
        int testCases = Integer.parseInt(args[1]);          

        Random rand = new Random();
        Vector<Vector<String>> BFS = new Vector<Vector<String>>();
        Vector<String> TreeGen = new Vector<String>();
        Vector<String> AllEmployees = new Vector<String>();
        HashMap<String, Integer> indexInAllEmployees = new HashMap<String, Integer>();
        Tree<String> tree = generate_random_tree(input_height, BFS, TreeGen, AllEmployees, indexInAllEmployees); /* Prints treegen to STDOUT */  /* Need to prepend the number of employees to treegen */
        int num_employees = TreeGen.size() + 1;
        System.out.println(num_employees);
        for(int i=0; i<(num_employees-1); i++){
            System.out.println(TreeGen.get(i));
        }

        System.out.println(testCases);
        /* Print number of testcases */
        /* Test case generation */
        for(int i = 0; i < testCases; i++){
            num_employees = AllEmployees.size();
            if(i%10==0){
                System.out.println(3);
                continue;
            }
            if(i%5==0){
                int emp_id1 = rand.nextInt(num_employees);
                int emp_id2 = rand.nextInt(num_employees);
                System.out.println("2 " + AllEmployees.get(emp_id1) + " " + AllEmployees.get(emp_id2));
                continue;
            }
            if(i%2==0){
                int emp_id1 = rand.nextInt(num_employees);
                String emp1 = AllEmployees.get(emp_id1);
                String name = RandomString.generate(7) + "_" + Integer.toString(i);
                System.out.println("0 " + name + " " + emp1);
                tree.insert(name, emp1);
                int new_level = tree.get_level(emp1) + 1;
                int num_level = BFS.size();
                if(new_level >= num_level){
                    Vector<String> curr_level = new Vector<String>();
                    BFS.add(curr_level);
                }
                BFS.get(new_level).add(name);
                AllEmployees.add(name);
                indexInAllEmployees.put(name,num_employees);
                continue;
            }
            int num_level = BFS.size();
            int level_get = rand.nextInt(num_level);
            while(BFS.get(level_get).size()<2){
                level_get = rand.nextInt(num_level);
            }
            int num_emp = BFS.get(level_get).size();
            int emp_id1 = rand.nextInt(num_emp);
            int emp_id2 = rand.nextInt(num_emp);
            while(emp_id1==emp_id2){
                emp_id2 = rand.nextInt(num_emp);
            }
            System.out.println("1 " + BFS.get(level_get).get(emp_id1) + " " + BFS.get(level_get).get(emp_id2));
            tree.delete_node(BFS.get(level_get).get(emp_id1),BFS.get(level_get).get(emp_id2));
            String last_emp = BFS.get(level_get).get(num_emp-1);
            int removedIndex = indexInAllEmployees.get(BFS.get(level_get).get(emp_id1));
            String lastName = AllEmployees.get(AllEmployees.size()-1);
            indexInAllEmployees.put(lastName, removedIndex);
            AllEmployees.set(removedIndex, lastName);
            AllEmployees.remove(AllEmployees.size()-1);
            BFS.get(level_get).set(emp_id1,last_emp);
            BFS.get(level_get).remove(num_emp-1);



            /* Resignation and assigning to an existing person */
            /* Resignation and assigned to new person */
            /* Find common ancestor */
            /* Print BFS Tree */
        }

        /* Run their assignment with the generated testfile */

        /* Checker */
    }
}