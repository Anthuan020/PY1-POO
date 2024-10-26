package XmlEditor;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import logicabancaria.Usuario;
import logicabancaria.Transaccion;
import logicabancaria.Cuenta;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class XmlController {

    private Document doc;
    private String filePath;

    // Constructor que carga el archivo XML
    public XmlController(String filePath) {
        this.filePath = filePath;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            File xmlFile = new File(filePath);
    
            // Si el archivo está vacío o mal formado, crea uno nuevo
            if (xmlFile.length() == 0) {
                System.out.println("El archivo XML está vacío. Creando nuevo documento.");
                doc = builder.newDocument();  // Crear un nuevo documento vacío
                Element rootElement = doc.createElement("root");  // Crear un nodo raíz
                doc.appendChild(rootElement);
            } else {
                // Leer el archivo XML solo si no está vacío
                doc = builder.parse(xmlFile);
                doc.getDocumentElement().normalize();
                System.out.println("Archivo XML cargado correctamente.");
            }
        } catch (SAXException e) {
            System.out.println("El archivo XML está mal formado: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error al leer el archivo XML: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para agregar un nuevo elemento al XML
    public void agregarElemento(String nombreElemento, String contenidoTexto) {
        Element nuevoElemento = doc.createElement(nombreElemento);
        nuevoElemento.appendChild(doc.createTextNode(contenidoTexto));

        // Agregar el nuevo elemento al nodo raíz
        doc.getDocumentElement().appendChild(nuevoElemento);

        System.out.println("Elemento agregado: " + nombreElemento);
    }

    // Método para editar el contenido de un elemento XML
    public void editarElemento(String nombreElemento, String nuevoContenido) {
        NodeList listaElementos = doc.getElementsByTagName(nombreElemento);

        // Si el elemento existe, se edita el primer nodo encontrado
        if (listaElementos.getLength() > 0) {
            Node nodo = listaElementos.item(0);
            nodo.setTextContent(nuevoContenido);
            System.out.println("Elemento editado: " + nombreElemento);
        } else {
            System.out.println("El elemento no fue encontrado: " + nombreElemento);
        }
    }

    // Método para eliminar un elemento del XML
    public void eliminarElemento(String nombreElemento) {
        NodeList listaElementos = doc.getElementsByTagName(nombreElemento);

        // Si el elemento existe, eliminar el primer nodo encontrado
        if (listaElementos.getLength() > 0) {
            Node nodo = listaElementos.item(0);
            nodo.getParentNode().removeChild(nodo);
            System.out.println("Elemento eliminado: " + nombreElemento);
        } else {
            System.out.println("El elemento no fue encontrado: " + nombreElemento);
        }
    }

    // Método para obtener el contenido de un elemento
    public String obtenerValorElemento(String nombreElemento) {
        NodeList listaElementos = doc.getElementsByTagName(nombreElemento);
        if (listaElementos.getLength() > 0) {
            Node nodo = listaElementos.item(0);
            return nodo.getTextContent();
        } else {
            return "Elemento no encontrado";
        }
    }

    // Método para listar todos los elementos en el documento
    public List<String> listarElementos() {
        List<String> nombresElementos = new ArrayList<>();
        NodeList todosElementos = doc.getElementsByTagName("*");  // Obtiene todos los elementos

        for (int i = 0; i < todosElementos.getLength(); i++) {
            Element elemento = (Element) todosElementos.item(i);
            nombresElementos.add(elemento.getTagName());  // Obtiene el nombre de la etiqueta
        }
        return nombresElementos;
    }

    // Método para obtener los atributos de un elemento
    public List<String> obtenerAtributosElemento(String nombreElemento) {
        NodeList listaElementos = doc.getElementsByTagName(nombreElemento);
        List<String> listaAtributos = new ArrayList<>();

        if (listaElementos.getLength() > 0) {
            Element elemento = (Element) listaElementos.item(0);
            NamedNodeMap mapaAtributos = elemento.getAttributes();

            for (int i = 0; i < mapaAtributos.getLength(); i++) {
                Node atributo = mapaAtributos.item(i);
                listaAtributos.add(atributo.getNodeName() + " = " + atributo.getNodeValue());
            }
        } else {
            System.out.println("El elemento no tiene atributos o no fue encontrado.");
        }

        return listaAtributos;
    }
    
    private Element crearElemento(String nombre, String value){
        Element nuevoElemento = doc.createElement(nombre);
        nuevoElemento.appendChild(doc.createTextNode(value));
        return nuevoElemento;
    }
    
    // Método para insertar un usuario en usuarios.xml (actualizado para incluir transacciones)
    public void insertarUsuario(Usuario usuario) {
        Element usuarioElement = doc.createElement("Usuario");
    
        usuarioElement.setAttribute("id", usuario.getId());
        usuarioElement.appendChild(crearElemento("Nombre", usuario.getName()));
        usuarioElement.appendChild(crearElemento("NumeroTelefono", usuario.getNumberTel()));
        usuarioElement.appendChild(crearElemento("Correo", usuario.getMail()));
    
        
        doc.getDocumentElement().appendChild(usuarioElement);
        guardarCambios();

    }
    
    public void insertarCuenta(Cuenta cuenta) {
        Element cuentaElement = doc.createElement("Cuenta");
    
        cuentaElement.setAttribute("id", String.valueOf(cuenta.getId()));
        cuentaElement.appendChild(crearElemento("Dueño", cuenta.getOwner().getName()));
        cuentaElement.appendChild(crearElemento("Pin", cuenta.getPin()));
        cuentaElement.appendChild(crearElemento("Balance", String.valueOf(cuenta.getBalance())));
            
        List<Transaccion> transacciones = cuenta.getTransacciones();
        if (!transacciones.isEmpty() && transacciones.size() > 0){
            for( Transaccion trans : transacciones){
                Element transElement = doc.createElement("Transacción");
                
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                
                transElement.setAttribute("id", String.valueOf(cuenta.getId()));
                transElement.appendChild(crearElemento("Tipo", trans.getType()));
                transElement.appendChild(crearElemento("Amount", String.valueOf(trans.getAmount())));
                transElement.appendChild(crearElemento("Fecha", trans.getDate().format(formato)));
                
                cuentaElement.appendChild(transElement);
            }
        }
        
        doc.getDocumentElement().appendChild(cuentaElement);
        guardarCambios();
    }
    
    public void limpiarXml() {
        try {
            // Crea un nuevo documento vacío
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
            
            // Agrega un nodo raíz vacío
            Element rootElement = doc.createElement("root");
            doc.appendChild(rootElement);
            
            // Guarda los cambios en el archivo
            guardarCambios();
            System.out.println("Archivo XML limpiado exitosamente.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al limpiar el archivo XML.");
        }
    }
    
    public NodeList lista(String non){
        return doc.getElementsByTagName(non);
    }
    
    
    public List<Usuario> extraerUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        NodeList usuariosNodos = doc.getElementsByTagName("Usuario");
        XmlController counts = new XmlController("Xmls/Cuentas.XML");

        for (int i = 0; i < usuariosNodos.getLength(); i++) {
            Node usuarioNode = usuariosNodos.item(i);
            if (usuarioNode.getNodeType() == Node.ELEMENT_NODE) {
                Element usuarioElement = (Element) usuarioNode;
                String id = usuarioElement.getAttribute("id");
                String nombre = usuarioElement.getElementsByTagName("Nombre").item(0).getTextContent();
                String telefono = usuarioElement.getElementsByTagName("NumeroTelefono").item(0).getTextContent();
                String correo = usuarioElement.getElementsByTagName("Correo").item(0).getTextContent();

                Usuario usuario = new Usuario(nombre, id, telefono, correo);

                // Extraer cuentas del usuario
                NodeList cuentasNodos = counts.lista("Cuenta");
                for (int j = 0; j < cuentasNodos.getLength(); j++) {
                    Element cuentaElement = (Element) cuentasNodos.item(j);
                    if ( cuentaElement.getElementsByTagName("Dueño").item(0).getTextContent() == usuario.getName()){
                        int cuentaId = Integer.parseInt(cuentaElement.getAttribute("id"));
                        double balance = Double.parseDouble(cuentaElement.getElementsByTagName("Balance").item(0).getTextContent());
                        String pin = cuentaElement.getElementsByTagName("Pin").item(0).getTextContent();
    
                        Cuenta cuenta = new Cuenta(usuario, pin, balance, cuentaId);
                        NodeList transacciones = cuentaElement.getElementsByTagName("Transacción");
                        if (transacciones.getLength() != 0){
                            for (int k = 0; k < transacciones.getLength(); k++){
                                Element trans = (Element) transacciones.item(j);
                                String pType = trans.getElementsByTagName("Tipo").item(0).getTextContent();
                                double pAmount = Double.parseDouble(trans.getElementsByTagName("Amount").item(0).getTextContent());
                                DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                LocalDate pDate = LocalDate.parse(trans.getElementsByTagName("Fecha").item(0).getTextContent(), formato);
                                double pComission = 0.0;
                                Transaccion transac = new Transaccion(pType, pAmount, pDate, pComission);
                                
                                cuenta.addTransaccion(transac);
                            }
                        }
                        
                        usuario.addAccount(cuenta);
                    }
                }

                usuarios.add(usuario);
            }
        }
        
        limpiarXml();
        counts.limpiarXml();
        
        
        return usuarios;
    }


    // Guardar los cambios en el archivo XML
    public void guardarCambios() {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");  // Para formato bonito con indentación
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));

            transformer.transform(source, result);  // Guarda los cambios en el archivo
            System.out.println("Cambios guardados en el archivo: " + filePath);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
