package com.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListSerializeUtil <M extends Serializable>{
	
	public void close(Closeable closeable) {
	    if (closeable != null) {
	      try {
	        closeable.close();
	      } catch (Exception e) {
	         
	      }
	    }
	  }
	//list反序列化
	@SuppressWarnings("unchecked")
	  public List<M> deserialize(byte[] in) {
	    List<M> list = new ArrayList<M>();
	    ByteArrayInputStream bis = null;
	    ObjectInputStream is = null;
	    try {
	      if (in != null) {
	        bis = new ByteArrayInputStream(in);
	        is = new ObjectInputStream(bis);
	        while (true) {
	          M m = (M)is.readObject();
	          if (m == null) {
	            break;
	          }
	          
	          list.add(m);
	          
	        }
	        is.close();
	        bis.close();
	      }
	    } catch (IOException e) {  
	    	
	  } catch (ClassNotFoundException e) {  
	     
	  }  finally {
	      close(is);
	      close(bis);
	    }
	    
	    return  list;
	  }
	  
	  //list序列化
	  @SuppressWarnings("unchecked")
	  public byte[] serialize(List<M> list) {
	    if (list == null)
	      throw new NullPointerException("Can't serialize null");
	    
	    List<M> values = list;
	    
	    byte[] results = null;
	    ByteArrayOutputStream bos = null;
	    ObjectOutputStream os = null;
	    
	    try {
	      bos = new ByteArrayOutputStream();
	      os = new ObjectOutputStream(bos);
	      for (M m : values) {
	        os.writeObject(m);
	      }
	      
	      // os.writeObject(null);
	      os.close();
	      bos.close();
	      results = bos.toByteArray();
	    } catch (IOException e) {
	      throw new IllegalArgumentException("Non-serializable object", e);
	    } finally {
	      close(os);
	      close(bos);
	    }
	    
	    return results;
	  }
}
