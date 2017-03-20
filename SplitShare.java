/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package splitshare;
import java.util.*;
/**
 *
 * @author Ankit Kodalikar
 */
public class SplitShare {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        // TODO code application logic here
        HashMap<String,Double>  expenses = new HashMap();
        HashMap<String,Double>  debts = new HashMap();
        expenses.put("Hari", Double.valueOf(150));
        expenses.put("Adya", Double.valueOf(200));
        expenses.put("Ankit", Double.valueOf(180));
        expenses.put("Pralobh", Double.valueOf(190));
        expenses.put("Sandip", Double.valueOf(0));
        SplitShare s = new SplitShare();
        double expenseByEach = s.getExpenseByEach(expenses);
        System.out.println("expense"+ expenseByEach);
        debts = s.debtByEach(expenseByEach, expenses);
        s.distribute(debts,expenseByEach);
    }

    private HashMap<String, Double> debtByEach(double expenseByEach, HashMap<String, Double> expenses) {
        HashMap<String, Double> debt = new HashMap();
        for (Map.Entry<String, Double> exp : expenses.entrySet()) {
            String name = exp.getKey();
            Double debtBy = exp.getValue();
            double temp = expenseByEach - debtBy;
            System.out.println("temp "+ temp);
            debt.put(name, temp);
        }
        
        System.out.println("debtMap "+ debt);
        return debt;
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
    }

    private double getExpenseByEach(HashMap<String, Double> expenses) {
        double expense = 0;
        for (Map.Entry<String, Double> entry : expenses.entrySet()) {
            String string = entry.getKey();
            Double double1 = entry.getValue();
            expense +=double1;
        }
        int size = expenses.size();
        expense = expense / size;
        return expense;
    }

    private HashMap<String, Double> distribute(HashMap<String, Double> debts, double expenseByEach) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      HashMap<String,HashMap<String, Double>> distribution = new HashMap(); 
      for (Map.Entry<String, Double> entry : debts.entrySet()) {
        String name = entry.getKey();
        Double debt = entry.getValue();
        HashMap<String, Double> t = new HashMap();
        if (debt > 0){
            
            for (Map.Entry<String, Double> innerEntry : debts.entrySet()) {
                double temp = 0;
                Double debt1 = innerEntry.getValue();
                String name1 = innerEntry.getKey();
                if(debt1 < 0){
                    temp = 0 - debt1;
                    System.out.println("temp here"+ temp);
                    if(temp > debt){
                        t.put(name1, debt);
                        debt = temp - debt;
                        temp = 0 - debt;
                        debt = Double.valueOf(0);
                        
                    }
                    else{
                        t.put(name1, temp);
                        debt = debt - temp;
                        temp = 0;
                    }
                    debts.put(name, debt);
                    debts.put(name1, temp);
                    
                    
                }
                
            }
            
        }
        distribution.put(name, t);    
      }
      System.out.println("debts over here is"+ debts);
        System.out.println("distribution"+ distribution);
      return null;  
    }
    
}
