<?php
 
    $name = $_POST["name"]; 
     $image = $_POST["image"];
    $response = array();
 
    $decodedImage = base64_decode("$image");
    $return = file_put_contents("img/".$name.".JPG", $decodedImage);
 
    if($return !== false){
        $response['success'] = 1;
        $response['message'] = "Image Uploaded Successfully with Retrofit";
    }else{
        $response['success'] = 0;
        $response['message'] = "Image Uploaded Failed";
    }
 
    echo json_encode($response);
?>
