import java.util.*;
import java.io.*;

public class company {
    public static bst employee = new bst();
    public static Node ceo = new Node();
    public static class Node{
        String name;
        int level;
        Node boss;
        List<Node> juniors = new Vector<Node>();
    }
    public static void AddEmployee(String a, String b) {       
        if(employee.get(a)!=null){
            System.out.println("Employee "+a+" already exists.");
            System.exit(0);
        }
        if(employee.get(b)==null){
            System.out.println("Employee "+ b+" doesn't exist.");
            System.exit(0);            
        }
        Node hire = new Node();
        hire.name = a;
        employee.put(hire.name,hire);
        hire.boss = employee.get(b);
        hire.boss.juniors.add(hire);
        hire.level = hire.boss.level +1;
    }
    public static void DeleteEmployee(String fire, String take) {    
        Node pre = employee.get(fire);
        Node post = employee.get(take);
        if(pre==null||post==null){                  
            System.out.println("Employee of name "+ take+" or "+fire+" doesn't exist.");
            System.exit(0);
        }
        int i=0;
        for(i=0;i<pre.juniors.size();i++){
            Node j = pre.juniors.get(i);
            post.juniors.add(j);
            j.boss = post;
        }
        if(pre.level!=post.level){
            System.out.println("levels of "+fire + " and "+ take+" not equal");
            System.exit(0);
        }
        pre.boss.juniors.remove(pre.boss.juniors.indexOf(pre));
        employee.remove(fire);
    }
    public static void LowestCommonBoss(String c, String d) {
        Node e1 = employee.get(c);
        Node e2 = employee.get(d);
        if(e1==null||e2==null){
            System.out.println(c+" or "+d+" don't exist");
            System.exit(0);
        }
        while(e1.level<e2.level) e2 = e2.boss;
        while(e2.level<e1.level) e1 = e1.boss;
        if(e2.name.equals(e1.name)) {               
            System.out.println(e2.boss.name);
            return;
        }
        while(e2.name!=e1.name){
            e2 = e2.boss;
            e1 = e1.boss;
        }
        System.out.println(e1.name);
    }
    public static void PrintEmployees(){
        class track{
            int stage;
            int lev;
            Node v;
            track(Node ve){
                v = ve;
                lev = ve.level;
                stage =0;
            }
        }
        Stack<track> stim = new Stack<track>();
        Stack<track> temp = new Stack<track>();
        track rep = new track(ceo);
        stim.push(rep);
        while(!stim.empty()){
            rep=stim.peek();
            stim.pop();
            if(rep.stage==0) System.out.println(rep.v.name);
            while(rep.stage<rep.v.juniors.size()){
                track fresh = new track(rep.v.juniors.get(rep.stage));
                while(!stim.empty() && fresh.lev>stim.peek().lev){
                    temp.push(stim.peek());
                    stim.pop();
                }
                stim.push(fresh);
                while(!temp.empty()){
                    stim.push(temp.peek());
                    temp.pop();
                }
                rep.stage++;
                stim.push(rep);
            }
        }
    }
    public static void main(String[] args) {
        try{ 
            File f = new File(args[0]);
            Scanner s = new Scanner(f);
            int num_employees = s.nextInt();
            String so = s.nextLine();
            if(so.equals("null 1g")) System.out.println(so+" wrong");
            int i=0;
            for(i=0;i<num_employees-1;i++){
                String[] x = s.nextLine().split(" ");
                if(i==0){
                    ceo.name = x[1];
                    ceo.level = 1;
                    employee.put(ceo.name,ceo);
                }
                AddEmployee(x[0], x[1]);
            }
            int num_commands = s.nextInt();
            int que[] = new int[num_commands];
            String emp1[] = new String[num_commands];
            String emp2[] = new String[num_commands];
            for(i=0;i<num_commands;i++){
                que[i] = s.nextInt();
                if(que[i]<3&&que[i]>=0){
                    String w[] = s.nextLine().split(" ");
                    emp1[i] = w[1];
                    emp2[i] = w[2];
                }
            }
            s.close();
            try{
                PrintStream ext = new PrintStream(new File("output.txt"));
                System.setOut(ext); 
                for(i=0;i<num_commands;i++){
                    if(que[i]==0) AddEmployee(emp1[i],emp2[i]);
                    else if(que[i]==1) DeleteEmployee(emp1[i], emp2[i]);
                    else if(que[i]==2) LowestCommonBoss(emp1[i], emp2[i]);
                    else if(que[i]==3) PrintEmployees();
                }
            }catch(FileNotFoundException z){
                System.out.println("Output file not found");
                System.exit(0);
            }
        }catch(FileNotFoundException y){
            System.out.println("Input file not found");
            System.exit(0);
        }
    }
}