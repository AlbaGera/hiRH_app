<?php
include 'conexion.php';
$usuario=$_POST['usuario'];
$contrasena=$_POST['password'];

//$usuario="luis@gmail.com";
//$contrasena="123456";
//$usu_usuario="aroncal@gmail.com";
//$usu_password="12345678";

$sentencia=$conexion->prepare("SELECT * FROM usuario WHERE usuario=? AND contrasena=?");
$sentencia->bind_param('ss',$usuario,$contrasena);
$sentencia->execute();

$resultado = $sentencia->get_result();
if ($fila = $resultado->fetch_assoc()) {
         echo json_encode($fila,JSON_UNESCAPED_UNICODE);     
}
$sentencia->close();
$conexion->close();
?>