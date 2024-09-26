
package peluqueria;




import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Statement;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;



public class Cliente {
    
    private String nombre;
    private String apellido;
    private String telefono;
    
    
    // Constructor
    
    public Cliente() {
    }

    public Cliente(String nombre, String apellido, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "\nCliente" + "\nnombre=" + nombre + "\napellido=" + apellido + "\ntelefono=" + telefono;
    }
    public void crearCliente(JTextField paramNombre, JTextField paramApellido, JTextField paramTelefono) {
        Conection conn = new Conection();
        Cliente cliente = new Cliente();
        cliente.setNombre(paramNombre.getText());
        cliente.setApellido(paramApellido.getText());
        cliente.setTelefono(paramTelefono.getText());
        
        
        try {
            String sql = "INSERT INTO `clientes` (`ID_cliente`, `nombre`, `apellido`, `telefono`) VALUES (NULL, ?, ?, ?)";
            CallableStatement cs = conn.conectar().prepareCall(sql);
            cs.setString(1, cliente.getNombre());
            cs.setString(2, cliente.getApellido());
            cs.setString(3, cliente.getTelefono());
            cs.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cliente creado exitosamente.");
            cs.close();
            System.out.println("Cliente creado exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "error al crear dato, error: "+e.toString());
        }
    }
    
    public void consultarCliente(JTable paramTablaCliente){
        Conection conn = new Conection();
        DefaultTableModel modelo = new DefaultTableModel();
        
        
        TableRowSorter<TableModel> OrdenarTabla= new TableRowSorter<>(modelo);
        paramTablaCliente.setRowSorter(OrdenarTabla);
        
        String sql="";
        modelo.addColumn("Id");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Telefono");
        paramTablaCliente.setModel(modelo);
        
        sql= "select * from `clientes`";
        String [] datos = new String[4]; 
        

        try {
        Statement statement;
        statement = conn.conectar().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        
            while (resultSet.next()) {
               datos[0]=resultSet.getString(1);
               datos[1]=resultSet.getString(2);
               datos[2]=resultSet.getString(3);
               datos[3]=resultSet.getString(4);
               
               modelo.addRow(datos);
                
            }
            
            paramTablaCliente.setModel(modelo);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "no se pudo mostrar los registros, error: "+e.toString());
        }
        
    }
    
    public void SeleccionarCliente(JTable paramTablaCliente,JTextField paramNombre, JTextField paramApellido, JTextField paramTelefono){
        
        try {
            int fila = paramTablaCliente.getSelectedRow();
            
            if (fila >=0) {
                paramNombre.setText(paramTablaCliente.getValueAt(fila, 1).toString());
                paramApellido.setText(paramTablaCliente.getValueAt(fila,2).toString());
                paramTelefono.setText(paramTablaCliente.getValueAt(fila, 3).toString());
            } else {
                JOptionPane.showMessageDialog(null, "Fila no selecionada");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error de seleccion"+e.toString());
        }
   
        
    }
    
    public void mostrarIdCliente(JComboBox comboBox,JTextField paramNombre, JTextField paramApellido, JTextField paramTelefono){
    Conection conn = new Conection();
    
        try {
            String sql = "SELECT * FROM clientes"; // Ajusta la consulta según tu estructura de base de datos
            Statement statement;
            statement = conn.conectar().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<Integer> idList = new ArrayList<>();
            while (resultSet.next()) {
            int id = resultSet.getInt("ID_cliente");
            idList.add(id);
            }
        DefaultComboBoxModel<Integer> idModel = new DefaultComboBoxModel<>(idList.toArray(new Integer[0]));
        comboBox.setModel(idModel);
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               int selectedId = (int) comboBox.getSelectedItem();
                // Obtener los datos del cliente basados en el ID seleccionado desde la base de datos
                Cliente cliente = obtenerClientePorId(selectedId);

                // Actualizar los campos de texto con los datos del cliente
                
                paramNombre.setText(cliente.getNombre());
                paramApellido.setText(cliente.getApellido());
                paramTelefono.setText(cliente.getTelefono()); 
 
            }
        });
      
        } catch (SQLException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public Cliente obtenerClientePorId(int id) {
    Conection conn = new Conection();
    Cliente cliente = null;

    try {
        String sql = "SELECT * FROM clientes WHERE ID_cliente = ?";
        PreparedStatement statement = conn.conectar().prepareStatement(sql);
        statement.setInt(1, id);
        
        ResultSet resultSet = statement.executeQuery();
        
        if (resultSet.next()) {
            String nombre = resultSet.getString("nombre");
            String apellido = resultSet.getString("apellido");
            String telefono = resultSet.getString("telefono");
            
            cliente = new Cliente(nombre, apellido, telefono);
        }

        statement.close();
    } catch (SQLException ex) {
        Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
    }

    return cliente;
}
    public void modificarCliente(JTextField paramNombre, JTextField paramApellido, JTextField paramTelefono, JComboBox comboBox){
        Conection conn = new Conection();
        Cliente cliente = new Cliente();
        int selectedId = (int) comboBox.getSelectedItem();
        cliente.setNombre(paramNombre.getText());
        cliente.setApellido(paramApellido.getText());
        cliente.setTelefono(paramTelefono.getText());
        String sql ="UPDATE `clientes` SET nombre = ?, apellido = ?, Telefono = ? WHERE ID_Cliente = ?";
        
        
        try {
            
            CallableStatement st = conn.conectar().prepareCall(sql);
            st.setString(1,cliente.getNombre());
            st.setString(2,cliente.getApellido());
            st.setString(3,cliente.getTelefono());
            st.setInt(4, selectedId);
            st.execute();
            JOptionPane.showMessageDialog(null, "alumno modificado exitosamente");
            st.close();
            
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "alumno modificado exitosamente"+e.toString());
        }
        
        
        
        
        
        
    }
}
