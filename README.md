# Retrofit_Image_Upload
Retrofit Image Upload System , Youtube Video Tutorial Section . 
Check out the phpcode.php file for 
PHP CODE :




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








************************************
EASY Way to upload image using retrofit 
<img src="https://tasnuvaoshin.com/Screenshot_1579297053.png" width="400" height="620">


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
    echo json_encode($response);?>



