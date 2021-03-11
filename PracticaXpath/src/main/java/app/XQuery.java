package app;

import java.util.Objects;
import java.util.Scanner;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XQueryService;

public class XQuery {
	private static Database db;
	private static XQueryService xquery;

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		try {
			db = (Database) Class.forName("org.exist.xmldb.DatabaseImpl").newInstance();
			Collection col = Objects
					.requireNonNull(db.getCollection("exist://localhost:8080/exist/xmlrpc/db/XPath", "admin", "admin")); // base de datos: XPath.xml
			xquery = Objects.requireNonNull((XQueryService) col.getService(XQueryService.SERVICE_NAME, null));

			ResourceIterator ri;

			ri = xquery.query(
					"for $v in distinct-values(/productos/produc/cod_zona) return ($v, count(/productos/produc[cod_zona = $v]))")
					.getIterator();

			while (ri.hasMoreResources()) {
				System.out.println();
				XMLResource zona = ((XMLResource) ri.nextResource());
				System.out.print("La zona " + zona.getContent());
				if (ri.hasMoreResources()) {
					XMLResource count = ((XMLResource) ri.nextResource());
					System.out.println(" tiene " + count.getContent() + " productos");
				}
			}

			ri = xquery.query(
					"for $v in distinct-values(/productos/produc/cod_zona) return element{ 'zona' || $v }{ /productos/produc[cod_zona = $v]/denominacion }")
					.getIterator();
			while (ri.hasMoreResources()) {
				System.out.println();
				XMLResource nodo = ((XMLResource) ri.nextResource());
				System.out.println(nodo.getContent());
			}

			ri = xquery.query(
					"for $v in distinct-values(/productos/produc/cod_zona) return ($v, /productos/produc[precio = max(/productos/produc[cod_zona = $v]/precio)]/denominacion/text())")
					.getIterator();
			while (ri.hasMoreResources()) {
				System.out.println();
				XMLResource nodo = ((XMLResource) ri.nextResource());
				System.out.print("En la zona " + nodo.getContent());
				if (ri.hasMoreResources()) {
					XMLResource den = ((XMLResource) ri.nextResource());
					System.out.println(", el producto más caro es " + den.getContent());
				}
			}

			ri = xquery.query("(<placa>{/productos/produc/denominacion[contains(., 'Placa Base')]}</placa>,"
					+ "<micro>{/productos/produc/denominacion[contains(., 'Micro')]}</micro>,"
					+ "<memoria>{/productos/produc/denominacion[contains(., 'Memoria')]}</memoria>,"
					+ "<otros>{/productos/produc/denominacion[not(contains(., 'Memoria') or contains(., 'Micro') or contains(., 'Placa Base'))]}</otros>)")
					.getIterator();
			while (ri.hasMoreResources()) {
				System.out.println();
				XMLResource nodo = ((XMLResource) ri.nextResource());
				System.out.println(nodo.getContent());
			}

			ri = xquery.query(
					"for $suc in /sucursales/sucursal return (data($suc/@codigo), count($suc/cuenta[data(@tipo)='AHORRO']), count($suc/cuenta[data(@tipo)='PENSIONES']))")
					.getIterator();
			while (ri.hasMoreResources()) {
				System.out.println();
				XMLResource nodo = ((XMLResource) ri.nextResource());
				System.out.print("La sucursal " + nodo.getContent());

				if (ri.hasMoreResources()) {
					XMLResource n2 = (XMLResource) ri.nextResource();
					System.out.print(" tiene " + n2.getContent() + " cuentas tipo AHORRO y ");
				}

				if (ri.hasMoreResources()) {
					XMLResource n2 = (XMLResource) ri.nextResource();
					System.out.println(n2.getContent() + " cuentas tipo PENSIONES");
				}
			}

			ri = xquery.query(
					"for $suc in /sucursales/sucursal return (data($suc/@codigo), $suc/director/text(), $suc/poblacion/text(), sum($suc/cuenta/saldodebe), sum($suc/cuenta/saldohaber))")
					.getIterator();

			while (ri.hasMoreResources()) {
				System.out.println();

				XMLResource res = (XMLResource) ri.nextResource();
				System.out.println("Código: " + res.getContent());

				if (ri.hasMoreResources()) {
					res = (XMLResource) ri.nextResource();
					System.out.println("Director: " + res.getContent());
				}

				if (ri.hasMoreResources()) {
					res = (XMLResource) ri.nextResource();
					System.out.println("Población: " + res.getContent());
				}

				if (ri.hasMoreResources()) {
					res = (XMLResource) ri.nextResource();
					System.out.println("Total saldodebe: " + res.getContent());
				}

				if (ri.hasMoreResources()) {
					res = (XMLResource) ri.nextResource();
					System.out.println("Total saldohaber: " + res.getContent());
				}
			}

			ri = xquery.query(
					"for $suc in /sucursales/sucursal[count(cuenta) > 3] return (data($suc/@codigo), $suc/director/text(), $suc/poblacion/text())")
					.getIterator();

			while (ri.hasMoreResources()) {
				System.out.println();

				XMLResource res = (XMLResource) ri.nextResource();
				System.out.println("Código: " + res.getContent());

				if (ri.hasMoreResources()) {
					res = (XMLResource) ri.nextResource();
					System.out.println("Director: " + res.getContent());
				}

				if (ri.hasMoreResources()) {
					res = (XMLResource) ri.nextResource();
					System.out.println("Población: " + res.getContent());
				}
			}

			ri = xquery.query(
					"for $suc in /sucursales/sucursal return (data($suc/@codigo), $suc/cuenta[saldodebe = max($suc/cuenta/saldodebe)]/*/text())")
					.getIterator();

			while (ri.hasMoreResources()) {
				System.out.println();

				XMLResource res = (XMLResource) ri.nextResource();
				System.out.println("Cuenta con más saldodebe de la sucursal " + res.getContent());

				if (ri.hasMoreResources()) {
					res = (XMLResource) ri.nextResource();
					System.out.println("Nombre: " + res.getContent());
				}

				if (ri.hasMoreResources()) {
					res = (XMLResource) ri.nextResource();
					System.out.println("Número: " + res.getContent());
				}

				if (ri.hasMoreResources()) {
					res = (XMLResource) ri.nextResource();
					System.out.println("Saldohaber: " + res.getContent());
				}

				if (ri.hasMoreResources()) {
					res = (XMLResource) ri.nextResource();
					System.out.println("Saldodebe: " + res.getContent());
				}
			}

			ri = xquery.query(
					"/sucursales/sucursal/cuenta[data(@tipo) = 'PENSIONES' and aportacion = max(/sucursales/sucursal/cuenta/aportacion)]")
					.getIterator();

			while (ri.hasMoreResources()) {
				System.out.println();

				XMLResource res = (XMLResource) ri.nextResource();
				System.out.println(res.getContent());
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
}
