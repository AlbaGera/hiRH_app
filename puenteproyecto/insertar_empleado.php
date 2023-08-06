<?php
include 'conexion.php';

if($_SERVER['REQUEST_METHOD']=='POST'){

    $idempleado=$_POST['idempleado'];
    $nombre=$_POST['nombre'];
    $correo=$_POST['correo'];
    $direccion=$_POST['direccion'];
    $telefono=$_POST['telefono'];
    $edad=$_POST['edad'];
    $puesto=$_POST['puesto'];
    $estatus=$_POST['estatus'];
    $imagen=$_POST['imagen'];

    $path= "img/$nombre.png";
    $actualpath="http://192.168.100.47/puenteproyecto/$path";

    $consulta = "INSERT INTO empleado(idempleado,nombre,correo,direccion,telefono,edad,puesto,estatus,imagen) 
    values('$idempleado','$nombre','$correo','$direccion','$telefono','$edad','$puesto','$estatus','$actualpath') ";

    //mysqli_query($conexion, $consulta) or die(mysqli_error($conexion));
    //mysqli_close($conexion);

    if(mysqli_query($conexion, $consulta)){

        file_put_contents($path, base64_decode($imagen));

        echo "SE SUBIO EXITOSAMENTE";
        mysqli_close($conexion);
    }else{
        echo "Error";
    }

}

?>