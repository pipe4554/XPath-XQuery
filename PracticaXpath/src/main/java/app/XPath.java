package app;

import java.util.Objects;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;

public class XPath {

	private static Database db;
	private static XPathQueryService xpath;

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		try {
			db = (Database) Class.forName("org.exist.xmldb.DatabaseImpl").newInstance();
			Collection col = Objects
					.requireNonNull(db.getCollection("exist://localhost:8080/exist/xmlrpc/db/XPath", "admin", "admin"));
			xpath = Objects.requireNonNull((XPathQueryService) col.getService(XPathQueryService.SERVICE_NAME, null));

			ResourceIterator ri;

			ri = xpath.query("/productos/produc/*[self::denominacion or self::precio]").getIterator();
			while (ri.hasMoreResources()) {
				XMLResource res = (XMLResource) ri.nextResource();
				System.out.println(res.getContent());
				System.out.println();
			}

			ri = xpath.query("/productos/produc[denominacion[contains(., 'Placa Base')]]").getIterator();

			while (ri.hasMoreResources()) {
				System.out.println();
				XMLResource nodo = ((XMLResource) ri.nextResource());
				System.out.println(nodo.getContent());
				System.out.println();
			}

			ri = xpath.query("/productos/produc[precio[text() > 60] and cod_zona[text() = 20]]").getIterator();

			while (ri.hasMoreResources()) {
				System.out.println();
				XMLResource nodo = ((XMLResource) ri.nextResource());
				System.out.println(nodo.getContent());
				System.out.println();
			}

			ri = xpath.query("count(/productos/produc[denominacion[contains(., 'Memoria')] and cod_zona[text() = 10]])")
					.getIterator();

			while (ri.hasMoreResources()) {
				System.out.println();
				XMLResource nodo = ((XMLResource) ri.nextResource());
				System.out.println("Hay " + nodo.getContent() + " productos «Memoria» de la zona 10.");
				System.out.println();
			}

			ri = xpath.query(
					"sum(/productos/produc[denominacion[contains(., 'Micro')]]/precio/text()) div count(/productos/produc[denominacion[contains(., 'Micro')]])")
					.getIterator();

			while (ri.hasMoreResources()) {
				System.out.println();
				XMLResource nodo = ((XMLResource) ri.nextResource());
				System.out.println("La media de los precios de los microprocesadores es " + nodo.getContent());
				System.out.println();
			}

			ri = xpath.query("/productos/produc[number(stock_minimo) > number(stock_actual)]").getIterator();

			while (ri.hasMoreResources()) {
				System.out.println();
				XMLResource nodo = ((XMLResource) ri.nextResource());

				NodeList nodosProduc = nodo.getContentAsDOM().getChildNodes();

				for (int i = 0; i < nodosProduc.getLength(); ++i) {
					Node n = nodosProduc.item(i);
					if (n.getLocalName() != null) {
						System.out.println(n.getLocalName() + " = " + n.getTextContent());
					}
				}
			}

			ri = xpath.query(
					"/productos/produc/*[(self::denominacion or self::precio) and number(../stock_minimo/text()) > number(../stock_actual/text()) and ../cod_zona/text() = 40]/text()")
					.getIterator();

			while (ri.hasMoreResources()) {
				System.out.println();
				XMLResource nodo = ((XMLResource) ri.nextResource());
				System.out.println("nombre = " + nodo.getContent());

				if (ri.hasMoreResources()) {
					XMLResource n2 = ((XMLResource) ri.nextResource());
					System.out.println("precio = " + n2.getContent());
				}
			}

			ri = xpath.query("/productos/produc[precio = max(/productos/produc/precio)]").getIterator();

			while (ri.hasMoreResources()) {
				System.out.println();
				XMLResource nodo = ((XMLResource) ri.nextResource());
				System.out.println(nodo.getContent());
			}

			ri = xpath.query("/productos/produc[precio = min(/productos/produc[cod_zona = 20]/precio)]").getIterator();
			while (ri.hasMoreResources()) {
				System.out.println();
				XMLResource nodo = ((XMLResource) ri.nextResource());
				System.out.println(nodo.getContent());
			}

			ri = xpath.query("/productos/produc[precio = max(/productos/produc[cod_zona = 10]/precio)]").getIterator();

			while (ri.hasMoreResources()) {
				System.out.println();
				XMLResource nodo = ((XMLResource) ri.nextResource());
				System.out.println(nodo.getContent());
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
}
