//package Project;

import java.awt.Color;

import java.sql.*;
import java.awt.EventQueue;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DataMining {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	DefaultTableModel model;
	
	static Scanner sc=new Scanner(System.in);
	private static double a[], b[];
	private static int binSize;
	private static int size;
	

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DataMining window = new DataMining();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public DataMining() 
	{
		initialize();
	}
	
	
	private void initialize() 
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 500);
		frame.setTitle("Data Cleaning");
        frame.getContentPane().setBackground(new Color(250, 240, 230));
        frame.getContentPane().setLayout(null);
        
        JLabel lblNewLabel = new JLabel("Database : ");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
        lblNewLabel.setBounds(30, 90, 110, 21);
        frame.getContentPane().add(lblNewLabel);
        
        JLabel lblNewLabel_1 = new JLabel("Table : ");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 17));
        lblNewLabel_1.setBounds(30, 140, 110, 21);
        frame.getContentPane().add(lblNewLabel_1);
        
        JLabel lblNewLabel_2 = new JLabel("Attribute : ");
        lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 17));
        lblNewLabel_2.setBounds(30, 190, 110, 21);
        frame.getContentPane().add(lblNewLabel_2);
        
        JLabel lblNewLabel_3 = new JLabel("Bin Size : ");
        lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 17));
        lblNewLabel_3.setBounds(30, 240, 110, 21);
        frame.getContentPane().add(lblNewLabel_3);
        
        textField = new JTextField();
        textField.setBounds(165, 238, 335, 30);
        textField.setBackground(new Color(255, 250, 240));
        frame.getContentPane().add(textField);
        textField.setColumns(10);
        
        JLabel lblNewLabel_3_1 = new JLabel("Binning Options  :\r\n");
        lblNewLabel_3_1.setFont(new Font("Tahoma", Font.BOLD, 17));
        lblNewLabel_3_1.setBounds(30, 305, 179, 21);
        frame.getContentPane().add(lblNewLabel_3_1);
        
        JButton btnNewButton = new JButton("Mean\r\n");
        btnNewButton.setBackground(new Color(255, 228, 196));
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnNewButton.setBounds(30, 345, 130, 35);
        frame.getContentPane().add(btnNewButton);
        
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		byBinMean();
        	}
        });
    
        JButton btnNewButton_1 = new JButton("Median");
        btnNewButton_1.setBackground(new Color(255, 228, 196));
        btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnNewButton_1.setBounds(200, 345, 130, 35);
        frame.getContentPane().add(btnNewButton_1);
        
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		byBinMedian();
        	}
        });
        
        JButton btnNewButton_1_1 = new JButton("Boundry\r\n");
        btnNewButton_1_1.setBackground(new Color(255, 228, 196));
        btnNewButton_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnNewButton_1_1.setBounds(370, 345, 130, 35);
        frame.getContentPane().add(btnNewButton_1_1);
        
        btnNewButton_1_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		byBinBoundaries();
        	}
        });
        
        JLabel lblNewLabel_4 = new JLabel("Data Cleaning For Noisy Data");
        lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_4.setBounds(200, 22, 400, 21);
        frame.getContentPane().add(lblNewLabel_4);
        
        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBackground(new Color(255, 250, 240));
        textField_1.setBounds(165, 88, 335, 30);
        frame.getContentPane().add(textField_1);
        
        textField_2 = new JTextField();
        textField_2.setColumns(10);
        textField_2.setBackground(new Color(255, 250, 240));
        textField_2.setBounds(165, 138, 335, 30);
        frame.getContentPane().add(textField_2);
        
        textField_3 = new JTextField();
        textField_3.setColumns(10);
        textField_3.setBackground(new Color(255, 250, 240));
        textField_3.setBounds(165, 188, 335, 30);
        frame.getContentPane().add(textField_3);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	public void byBinMean()
	{
		
		binSize = Integer.parseInt(textField.getText());
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+textField_1.getText()+"", "root", "");
			
			Statement s = con.createStatement();
			String query = "Select count(*) from "+textField_2.getText()+"";
			ResultSet rs = s.executeQuery(query);
			
			rs.next();
			size = rs.getInt("count(*)");
			
			query = "Select * from "+textField_2.getText()+"";
			rs = s.executeQuery(query);
			
			a = new double[size];
			b = new double[size];
			int i = 0;
			while(rs.next()) {
				a[i]=rs.getDouble(""+textField_3.getText()+"");
				i++;
			}
			
			Arrays.sort(a);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		double sum = 0;
		int index=0;
		int bin_count = a.length / binSize;
	    for (int i = 0; i < bin_count; i++)
	    {
	        for (int j = 0; j < binSize; j++)
	        {
	            sum = sum + a[(i * binSize) + (j)];
	        }
	        for (int k = 0; k < binSize; k++)
	        {
	            b[index]=sum/binSize;
	            index++;
	        }
	        sum = 0;
	    }
	    
	    
	    try {
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+textField_1.getText()+"", "root", "");
			
			PreparedStatement ps = con.prepareStatement("UPDATE "+ textField_2.getText() +" SET "+textField_3.getText()+" = ? "+ "Where id = ?");
			
			for(int i=0;i<size;i++) 
			{
				ps.setDouble(1, b[i]);
				ps.setInt(2, i+1);
				ps.executeUpdate();
			}
		
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	    
	    System.out.println("By Bin Mean");
	    
	    for(int i=0;i<size;i++)
		{
			System.out.println(b[i]);
		}
	    
	    System.out.println("..................................");
	}
	
	
	public void byBinMedian()
	{
		
		binSize = Integer.parseInt(textField.getText());
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+textField_1.getText()+"", "root", "");
			
			Statement s = con.createStatement();
			String query = "Select count(*) from "+textField_2.getText()+"";
			ResultSet rs = s.executeQuery(query);
			
			rs.next();
			size = rs.getInt("count(*)");
			
			query = "Select * from "+textField_2.getText()+"";
			rs = s.executeQuery(query);
			
			a = new double[size];
			b = new double[size];
			int i = 0;
			while(rs.next()) {
				a[i]=rs.getDouble(""+textField_3.getText()+"");
				i++;
			}
			
			Arrays.sort(a);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		int index=0;
		int bin_count = a.length / binSize;
		int count=0;
		int position=0;

		if(binSize%2==1)
		{
			position=(binSize+1)/2;

			for (int i=0;i<bin_count; i++)
		    {
		        int t=count*binSize;
		        for (int j = t; j < (t + binSize); j++)
	            {
	                if(j==position-1)
	                {
	                	position=position+binSize;
	                	count++;
	                	for(int k=0;k<binSize;k++)
	                	{
	                		b[index]=a[j];
	                		index++;
	                	}
	                	break;
	                }

	            }
		    }
		}				
	    else
		{
			position=(binSize/2);

			for(int i=0;i<bin_count;i++)
			{
				int t=count*binSize;
				for(int j=t;j<(t+binSize);j++)
				{
					if(j==position-1)
					{
						double temp=(double)((a[j]+a[j+1])/2.0);
						position=position+binSize;
	                	count++;
						for(int k=0;k<binSize;k++)
	                	{
	                		b[index]=temp;
	                		index++;
	                	}
	                	break;
					}
				}
			}
		}
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+textField_1.getText()+"", "root", "");
			
			PreparedStatement ps = con.prepareStatement("UPDATE "+ textField_2.getText() +" SET "+textField_3.getText()+" = ? "+ "Where id = ?");
			
			for(int i=0;i<size;i++) 
			{
				ps.setDouble(1, b[i]);
				ps.setInt(2, i+1);
				ps.executeUpdate();
			}
		
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("By Bin Median");
		
		for(int i=0;i<size;i++)
		{
			System.out.println(b[i]);
		}
	    
	    System.out.println("..................................");
	     
	}
	
	
	public void byBinBoundaries()
	{
		binSize = Integer.parseInt(textField.getText());
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+textField_1.getText()+"", "root", "");
			
			Statement s = con.createStatement();
			String query = "Select count(*) from "+textField_2.getText()+"";
			ResultSet rs = s.executeQuery(query);
			
			rs.next();
			size = rs.getInt("count(*)");
			
			query = "Select * from "+textField_2.getText()+"";
			rs = s.executeQuery(query);
			
			a = new double[size];
			b = new double[size];
			int i = 0;
			while(rs.next()) {
				a[i]=rs.getDouble(""+textField_3.getText()+"");
				i++;
			}
			
			Arrays.sort(a);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		int index=0;
		int bin_count = a.length / binSize;
		for (int i = 0; i < bin_count; i++)
	    {
	        for (int j = 0; j < binSize; j++)
	        {
	            if ((a[(i *binSize) + (j)]-a[binSize*i]) < (a[(binSize * (i+1)) - 1]-a[(i * binSize) + (j)]))
	            {
	                b[index]=a[binSize * i];
	            }
	            else
	            {
	            	b[index]=a[(binSize * (i+1)) - 1];
	            }
	            index++;
	        }
	    }
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+textField_1.getText()+"", "root", "");
			
			PreparedStatement ps = con.prepareStatement("UPDATE "+ textField_2.getText() +" SET "+textField_3.getText()+" = ? "+ "Where id = ?");
			
			for(int i=0;i<size;i++) 
			{
				ps.setDouble(1, b[i]);
				ps.setInt(2, i+1);
				ps.executeUpdate();
			}
		
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("By Bin Boundaries");
		
		for(int i=0;i<size;i++)
		{
			System.out.println(b[i]);
		}
	    
	    System.out.println("..................................");
	}
	
}
