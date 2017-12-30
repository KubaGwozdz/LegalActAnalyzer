/**
 * Created by kuba on 01.12.2017
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Tree<T> {
    private Node<T> root;

    public Tree(T rootData){
        root = new Node<T>(rootData);
        root.parent = root;
        root.children = new LinkedList<>();
    }

// --------------  NODE  ----------------------------
    public static class Node<T> {
        private T data;
        private Node<T> parent;
        private List<Node<T>> children;


        public Node(T data){
            this.data = data;
        }

        public T getData() {
            return data;
        }


        public Node<T> getParent() {
            return parent;
        }

        public void showNode (){
            System.out.println(this.data);
        }

        public List<Node<T>> getChildren (){
            return this.children;
        }

        public Node<Act> findChild(Node<Act> parent, CmdLineParser.Argument argument){
            Node<Act> node = parent;
            for(int i=0;i<parent.getChildren().size();i++){
                Node<Act> child = parent.getChildren().get(i);
                if (child.getData().getStructure()==argument.getcStruct() && child.getData().getNum() == argument.getNum()){
                   node = child;
                }
            }
            return node;
        }
}
// --------------------------------------------------

    public Node<T> root(){
        return root;
    }

    public void add(Node<T> parent, int num, String name, T data){
        Node<T> act = new Node<T>(data);
        act.children = new LinkedList<>();
        parent.children.add(act);
        act.parent=parent;
    }

    public Node<T> addN(Node<T> parent, int num, String name, T data){
        Node<T> act = new Node<T>(data);
        act.children = new LinkedList<>();
        parent.children.add(act);
        act.parent=parent;
        return act;
    }

    public void show(Node<T> node){
        node.showNode();
        if(!node.children.isEmpty()){
            for (Node child :node.children){
                show(child);
            }
        }
    }


    public void showTableOfContents(Node<Act> node){
        if(node.data.getUStructure()==null) {
            if (node.data.getStructure() == ConstStructure.Constitution)
                System.out.println(node.data.getName());
            else if (node.data.getStructure() == ConstStructure.Chapter)
                System.out.println(" " + node.data.getName());
            else if (node.data.getStructure() == ConstStructure.Article)
                System.out.println("  " + node.data.getName());
            else if (node.data.getStructure() == ConstStructure.Section)
                System.out.println("   " + node.data.getName());
            else if (node.data.getStructure() == ConstStructure.Point)
                System.out.println("    " + node.data.getName());
        }
        else {
            if (node.data.getUStructure() == UokikStructure.Statute)
                System.out.println(node.data.getName());
            else if (node.data.getUStructure() == UokikStructure.Part)
                System.out.println(" " + node.data.getName());
            else if (node.data.getUStructure() == UokikStructure.Chapter)
                System.out.println("  " + node.data.getName());
            else if (node.data.getUStructure() == UokikStructure.Article)
                System.out.println("   " + node.data.getName());
            else if (node.data.getUStructure() == UokikStructure.Section)
                System.out.println("    " + node.data.getName());
            else if (node.data.getUStructure() == UokikStructure.Point)
                System.out.println("     " + node.data.getName());
            else if (node.data.getUStructure() == UokikStructure.SubPoint)
                System.out.println("      " + node.data.getName());
        }
        if(!node.children.isEmpty()){
            for (Node<Act> child :node.children){
                showTableOfContents(child);
            }
        }
    }



}