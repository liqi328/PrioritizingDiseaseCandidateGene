package graph;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Path<E> implements Iterable<E>{
	private List<E> path = new LinkedList<E>();
	private Class<E> type;
	public Path(Class<E> type){
		this.type = type;
	}
	
	public void add(E node){
		path.add(0, node);
	}
	
	public Iterator<E> iterator(){
		return path.iterator();
	}
	
	public E[] toArray(){
		//E[] array = (E[]) path.toArray(new E[]);
		E[] array = (E[]) Array.newInstance(type, path.size());
		int i = 0;
		for(E e : path){
			array[i++] = e;
		}
		return array;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for(E e : path){
			sb.append(e).append(" - ");
		}
		return sb.toString();
	}
}