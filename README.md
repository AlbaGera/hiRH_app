# proyecto hi!RH
## Aplicación móvil

1. Tener instalado XAMPP, Android Studio y VsualStudio.
2. Meter la carpeta "puenteproyecto" al aparto htdocs de xampp.

`nxampp/htdocs/puenteproyecto`

3. Abrir el proyecto "hiRH" en Android Studio.

## CMD
1.identificar la dirección IP de tu equipo.

`ipconfig /all`

## Android Studio
1. Modificar todas las direcciones IP de los metodos onlick de los botones.
   
### Ejemplo

`btnbuscar.setOnClickListener(new View.OnClickListener() {
   @Override
   public void onClick(View view) {
      buscarProducto("http://192.168.100.47/puenteproyecto/buscar.php?id_ticket="+edtnumticket.getText()+"");
   }
});`

## Data Base
1. Importar DB a phpmyadmyn.

## Navegador
1. Verificar que los documentos `.php ` de la carpeta `xampp/htdocs/puenteproyecto` funcionen correctamente.

# Ejecutar la aplicación uwu

