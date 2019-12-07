//--------------------------//
// INIT SOME CONFIGURATIONS //
//--------------------------//
var choiceids = document.getElementById("ids").getAttribute('data-src').split(',');
var soundfile = document.getElementById("soundfile").getAttribute('data-src');
document.getElementById("winner").value = 0xB16C0DE;
var context=main_window.getContext('2d');
var width=main_window.width;
var height=main_window.height;
var friction=0.98;
var nb = choiceids.length;
//var red = new player(0x51E77E,
//    new avatar("Red player",3*width/4,height/2,height/2/nb,"#FF0000","#FF2400"),
//    new keys(0x51,0x44,0x5A,0x53)
//    );
var blue = new player(0xED1C7E,
    new avatar("Player",width/2,height/2,height/2/nb,"#0000FF","#0066FF"),
    new keys(0x25,0x27,0x26,0x28)
    );
var goal = {x:width-blue.avatar.radius,y:height/3,
  width:width-blue.avatar.radius/2,height:height/3};
var players = new Array(blue);
for (var i=0;i<choiceids.length;i++) {
  var x = Math.floor((Math.random() * width/2) + height/2/nb);
  var y = Math.floor((Math.random() * 8*height/10) + height/10);
  var newPlayer = new player(0xBADDAD,
    new avatar("Choice #"+choiceids[i],x,y,height/2/nb,"#000000","#505050"),
    new keys(0x00,0x00,0x00,0x00)
  );
  players.push(newPlayer);
}
//--------------------------//
// END OF CONFIGURATIONS    //
//--------------------------//

function avatar(name,x,y,r,c,bc){
  this.name=name;
  this.x=x;
  this.y=y;
  this.radius=r;
  this.color=c;
  this.bordercolor=bc;
}

function keys(l,r,u,d){
  this.left={code:l,hold:false};
  this.right={code:r,hold:false};
  this.up={code:u,hold:false};
  this.down={code:d,hold:false};
}

function player(id, avatar, keys){
  this.id=id;
  this.avatar=avatar;
  this.keys=keys;
  this.vx=0;
  this.vy=0;

  this.updateFriction=updateFriction;
  function updateFriction(){ this.vx*=friction; this.vy*=friction; }

  this.updateCommands=updateCommands;
  function updateCommands(){ 
    if(this.keys.left.hold){this.vx--;}
    if(this.keys.right.hold){this.vx++;}
    if(this.keys.up.hold){this.vy--;}
    if(this.keys.down.hold){this.vy++;}
  }

  this.updateCollisionBorder=updateCollisionBorder;
  function updateCollisionBorder(){ 
    if (collisionLeftBorder(avatar)){ this.vx*=-1; this.avatar.x=this.avatar.radius; return true; }
    if (collisionRightBorder(avatar)){ this.vx*=-1; this.avatar.x=width-this.avatar.radius; return true; }
    if (collisionTopBorder(avatar)){ this.vy*=-1; this.avatar.y=this.avatar.radius; return true; }
    if (collisionBottomBorder(avatar)){ this.vy*=-1; this.avatar.y=height-this.avatar.radius; return true; }
    return false;
  }

  this.updateCollisionSameMass=updateCollisionSameMass;
  function updateCollisionSameMass(otherPlayer){
    if(collisionCircles(this.avatar, otherPlayer.avatar)){
      var x1=this.avatar.x;
      var y1=this.avatar.y;
      var r1=this.avatar.radius;
      var x2=otherPlayer.avatar.x;
      var y2=otherPlayer.avatar.y;
      var r2=otherPlayer.avatar.radius;
      var d=Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
      var nx = (x2 - x1)/(r1+r2);
      var ny = (y2 - y1)/(r1+r2);
      var gx = -ny;
      var gy = nx;
      var v1n = nx*this.vx + ny*this.vy;
      var v1g = gx*this.vx + gy*this.vy;
      var v2n = nx*otherPlayer.vx + ny*otherPlayer.vy;
      var v2g = gx*otherPlayer.vx + gy*otherPlayer.vy;
      this.vx = nx*v2n +  gx*v1g;
      this.vy = ny*v2n +  gy*v1g;
      otherPlayer.vx = nx*v1n +  gx*v2g;
      otherPlayer.vy = ny*v1n +  gy*v2g;

      otherPlayer.avatar.x = x1 + (r1+r2)*(x2-x1)/d;
      otherPlayer.avatar.y = y1 + (r1+r2)*(y2-y1)/d;
      return true;
    }
    return false;
  }

  this.updateCollisionInfiniteMass=updateCollisionInfiniteMass;
  function updateCollisionInfiniteMass(otherPlayer){
    if(collisionCircles(this.avatar,otherPlayer.avatar)){
      var x1=otherPlayer.avatar.x;
      var y1=otherPlayer.avatar.y;
      var r1=otherPlayer.avatar.radius;
      var x2=this.avatar.x;
      var y2=this.avatar.y;
      var r2=this.avatar.radius;
      var d=Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
      var nx = (x2 - x1)/(r1+r2);
      var ny = (y2 - y1)/(r1+r2);
      var pthis = this.vx*nx+this.vy*ny;
      this.vx = this.vx - 2*pthis*nx;
      this.vy = this.vy - 2*pthis*ny;

      this.avatar.x = x1 + (r1+r2)*(x2-x1)/d;
      this.avatar.y = y1 + (r1+r2)*(y2-y1)/d;
      return true;
    }
    return false;
  }

  this.updatePosition=updatePosition;
  function updatePosition(){ this.avatar.x+=this.vx; this.avatar.y+=this.vy; }

  this.draw=draw;
  function draw(){
    context.beginPath();
    var g=context.createRadialGradient(this.avatar.x,this.avatar.y,this.avatar.radius*0.98,this.avatar.x,this.avatar.y,this.avatar.radius);
    g.addColorStop(0,this.avatar.color);
    g.addColorStop(1,this.avatar.bordercolor);
    context.fillStyle=g;
    context.arc(this.avatar.x,this.avatar.y,
        this.avatar.radius,0,Math.PI*2,true);
    context.fill();
    context.fillStyle=blue.avatar.color;
    context.textAlign="center";
    context.font="14px Arial";
    context.fillText(this.avatar.name,this.avatar.x,this.avatar.y);
    context.closePath();
  }
}

document.onkeydown = function(event) {
  var key_pressed;
  if(event == null){
    key_pressed = window.event.keyCode;
  } else {
    key_pressed = event.keyCode;
  }
  for (var i=0;i<players.length;i++) {
    for (key in players[i].keys) {
      if (key_pressed == players[i].keys[key].code){
        players[i].keys[key].hold=true;
      }
    }
  }
}

document.onkeyup = function(event) {
  var key_pressed;
  if(event == null){
    key_pressed = window.event.keyCode;
  } else {
    key_pressed = event.keyCode;
  }
  for (var i=0;i<players.length;i++) {
    for (key in players[i].keys) {
      if (key_pressed == players[i].keys[key].code){
        players[i].keys[key].hold=false;
      }
    }
  }
}

function on_enter_frame(){
  var gameOver=false;

  for (var i=1;i<players.length;i++) {
    if (collisionCircleBox(players[i].avatar,goal)) {
      winnerid=choiceids[i-1];
      gameOver=true;
      document.getElementById("winner").value = winnerid;
      break;
    }
  }

  if(gameOver){
    context.fillStyle=blue.avatar.color;
    context.textAlign="center";
    context.font="50px Arial";
    context.fillText("Choice #"+winnerid+" wins!",width/2,height/2);
  } else {
    var collisionCheck=false;
    for (var i=0;i<players.length;i++) {
      players[i].updateFriction();
      players[i].updateCommands();
      collisionCheck|=players[i].updateCollisionBorder();
    }

    for (var i=0;i<players.length;i++) {
      for (var j=i+1;j<players.length;j++) {
        collisionCheck|=players[i].updateCollisionSameMass(players[j]);
      }
    }

    if(collisionCheck){onCollision();}

    context.clearRect(0,0,width,height);
    context.fillStyle=blue.avatar.color;
    context.fillRect(goal.x,goal.y,goal.width,goal.height);

    for (var i=players.length-1;i>-1;i--) {
      players[i].updatePosition();
      players[i].draw();
    }
  }
}
function log(msg) {
  setTimeout(function() {
    throw new Error(msg);
  }, 0);
}
function onCollision(){
  //var audio = new Audio();   
  //audio.src = soundfile;
  //audio.controls = true;
  //audio.autoplay = true;
  //log("bing!");
  return;
}
