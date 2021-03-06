/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jenudai;

/**
 *
 * @author Sandeep
 */
import java.sql.*;
import javax.swing.table.TableModel;
import net.proteanit.sql.DbUtils;

public  class JDBCConnection {
   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost:3306/jenuDai";

   //  Database credentials
   static final String USER = "root";
   static final String PASS = "root";
   public static Connection conn=null;
   
   public  void getConnection() {
   Statement stmt = null;
   try{
      //STEP 2: Register JDBC driver
      Class.forName("com.mysql.jdbc.Driver");

      //STEP 3: Open a connection
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);

   }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
//   }finally{
//      //finally block used to close resources
//      try{
//         if(stmt!=null)
//            stmt.close();
//      }catch(SQLException se2){
//      }// nothing we can do
//      try{
//         if(conn!=null)
//            conn.close();
//      }catch(SQLException se){
//         se.printStackTrace();
//      }//end finally try
   }//end try
}//end main
   public static void populate(javax.swing.JComboBox name,String type){
        name.insertItemAt("", 0);
        try{
           Statement stmt = conn.createStatement();
      String sql;
      sql = "SELECT distinct "+type+" FROM jenudai where "+type+"!='NULL'";
      ResultSet rs = stmt.executeQuery(sql);

      //STEP 5: Extract data from result set
      while(rs.next()){
          name.addItem(rs.getString(type));
      }

        }  
        catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
        }
    }
   public static void sumFields(javax.swing.JLabel name,String type){
        try{
           Statement stmt = conn.createStatement();
      String sql;
      sql = "SELECT SUM(cash) FROM jenudai where type='"+type+"' AND flag=1";
      String sql1="SELECT SUM(credit) FROM jenudai where type='"+type+"' AND flag=1";
      ResultSet rs = stmt.executeQuery(sql);
      float sum=0;
      

      //STEP 5: Extract data from result set
      while(rs.next()){
          if(rs.getString(1)!=null)
          sum=Float.parseFloat(rs.getString(1));
      }
      rs=stmt.executeQuery(sql1);
      if(rs.next()){
          if(rs.getString(1)!=null)
            sum+=Float.parseFloat(rs.getString(1));
      }
      
      name.setText(String.valueOf(sum));

        }  
        catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
        }
    }
   public static void loadData(javax.swing.JTable name,String actualQuery,String groupBy,int flag){
        String sql="";
        if(groupBy==null){
            sql = "select Date,invoice,customer,type,goods,qty,pricePerUnit,total from jenudai WHERE flag="+flag+" AND invoice="+actualQuery;
        }else{
             sql = "select "+actualQuery+" from jenudai WHERE flag="+flag+" group by "+groupBy;  
  
        }
        try{
            Statement  stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            TableModel table=DbUtils.resultSetToTableModel(rs);
            name.setModel(table);
        }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }  
    }


public static int customLoadData(javax.swing.JTable name,javax.swing.JTextField cash,String invoice){
    
 try{
           Statement stmt = conn.createStatement();
      String sql;
      sql ="select * from jenudai WHERE flag=0 AND invoice="+invoice;
      ResultSet rs = stmt.executeQuery(sql); 
      int i=0;

      //STEP 5: Extract data from result set
      while(rs.next()){
         name.setValueAt(rs.getString("goods"), i, 0);
         name.setValueAt(rs.getString("pricePerUnit"), i, 1);
         name.setValueAt(rs.getString("qty"), i, 2);
         name.setValueAt(rs.getString("total"), i, 3);
         i++;
      }
      sql="select * from jenudai WHERE flag=1 AND invoice="+invoice+" ORDER BY id";
       rs = stmt.executeQuery(sql); 

      while(rs.next()){
          cash.setText(rs.getString("cash"));
          return rs.getInt("id");
          
      }
        }  
        catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
        }
        return -1;
}

public static void delete(String invoice,int id){
    try{
           Statement stmt = conn.createStatement();
           String sql="DELETE FROM jenudai where invoice="+invoice;

       stmt.executeUpdate(sql); 
        sql="DELETE FROM jenudai where id="+id;
       stmt.executeUpdate(sql); 
   }  
        catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
        }
    
    
}
}