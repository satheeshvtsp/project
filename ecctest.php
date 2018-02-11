<?php
function invmod($a,$n){
	 if ($n < 0) $n = -$n;
	 if ($a < 0) $a = bcsub($n , bcmod(-$a , $n));
	 $t = 0; $nt = 1; $r = $n; $nr = bcmod($a , $n);
	 while ($nr != 0) {
		 $quot=  bcdiv($r,$nr);
		 $tmp = $nt;  $nt = bcsub($t , bcmul($quot,$nt));  $t = $tmp;
		 $tmp = $nr;  $nr = bcsub($r , bcmul($quot,$nr));  $r = $tmp;
	}
    if ($r > 1) return -1;
	if ($t < 0) $t += $n;
	return $t;
}
class ECC{
var $q,$a,$b,$zero;
function __construct($a,$b,$q){
$this->a=$a;
$this->b=$b;
$this->q=$q;
$this->zero=array(0,0);
//echo "<br/>q=>".$this->q;
}
function at($x){
	$ys=bcmod(bcadd(bcadd(bcpow($x,3),bcmul($this->a,$x)),$this->b),$this->q);
	$res=0;
for($i=0;$i<$this->q;$i++){
if(bcmod(bcmul($i,$i),$this->q)==$ys){
$res=$i;
return array($x,$i);
break;
}
}
if($res==0){
	
return false;
}
else 
return array($x,$i);

}
function order($g){
$flag=0;$res=1;
$p1=0;$p2=0;
for($i=2;$i;$i++){
$multi=$this->mul($g,$i);
if($i==2){
$p1=$multi[0];$p2=$multi[1];}
echo "<br/>$i g=>".$multi[0]." ".$multi[1];//."p1".$p1." ".$p2;
if($i!=2&&$multi[0]==$p1&&$multi[1]==$p2){
$res=$i;
$flag=1;
break;
}}

//echo "<br/>flag".$flag;
if($flag==1){
return $res;}
else 
{
return false;
}
}	
function add($p1,$p2){
	
	 if ($p1 == $this->zero)
            return $p2;
        if ($p2 == $this->zero)
            return $p1;
        if ($p1[0]==$p2[0] && $p2[1]==-$p1[1])
       {    // # p1 + -p1 == 0
            return $this->zero;
		}
		$lemta=0;
		if($p1[0]!=$p2[0]&&$p1[1]!=-$p2[1]){
			$lemta=bcmod(bcmul(bcmod(bcadd(bcsub($p2[1],$p1[1]),$this->q),$this->q),(invmod(bcmod(bcadd(bcsub($p2[0],$p1[0]),$this->q),$this->q),$this->q))),$this->q);
		//	echo "<br/> lemta if	".$lemta." ".$p1[0]." ".$p1[1]." ".$p2[0]." ".$p2[1]." ".$this->a." ".$this->b." ".$this->q." ".($p2[1]-$p1[1])." ".($p2[0]-$p1[0]);
			
		}else{
		
			$lemta=bcmod(bcmul(bcmod(bcadd(bcmul(3,bcpow($p1[0],2)),$this->a),$this->q),(invmod(bcmod(bcmul(2,$p1[1]),$this->q),$this->q))),$this->q);
		//		echo "<br/> lemta else	".$lemta." ".$p1[0]." ".$p1[1]." ".$p2[0]." ".$p2[1]." ".$this->a." ".$this->b." ".$this->q." ".(3*pow($p1[0],2)+$this->a)%$this->q." ".(2*$p1[1])%$this->q;	
		}
		$p3=array(0,0);

	//	$p3[0]=(($lemta*$lemta) − $p1[0] − $p2[0]) % $this->q;
	
	
		$p3[0]=bcmod(bcsub(bcsub(bcmul($lemta,$lemta),$p1[0]),$p2[0]),$this->q);
$p3[1]=bcmod(bcsub(bcmul($lemta,bcsub($p1[0],$p3[0])),$p1[1]),$this->q);

if($p3[0]<0){$p3[0]=bcadd($this->q,$p3[0]);}
if($p3[1]<0){$p3[1]=bcadd($this->q,$p3[1]);}
//echo "<br/>$p3=>".$p3[0]." ".$p3[1];
return $p3;
}

function mul($p1,$n)
{
	$r=array(0,0);
	$m2=$p1;
	if(bccomp($n,0)==-1){echo "</br> n error";}
	/*while($n>0){
	 if ($n & 1 == 1){
                $r = $this->add($r, $m2);
}
             $n = $n >> 1 ;
			 $m2=$this->add($m2, $m2);*/
			 
			 for($i=1;bccomp($i,bcadd($n,1))==-1;$i=bcadd($i,1)){
				   $r = $this->add($m2, $r);
			 }



return $r;
}
}
$a=-1;
$b=188;
$q=751;
while(bcsub((bcmul(4,$a)),(bcmul(27,$b)))==0){
	$a=bcadd($a,1);
    $b=bcadd($b,1);
}
echo "a=".$a." b=".$b." q=".$q;
$ec=new ECC($a,$b,$q);
$g=$ec->at(3);
echo "<br/>g=>".$g[0]." ".$g[1];
$n=$ec->order($g);
echo "<br/> n=".$n;
$privatekey=rand(1,$n);
$publickey=$ec->mul($g,$privatekey);
echo "<br/>publickey=".$publickey[0]. " ". $publickey[1];
echo "<br/>privatekey=".$privatekey;
?>