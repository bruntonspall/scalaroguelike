package uk.co.bruntonspall.roguelike.model

trait State
case object ReadyToStart extends State
case object InGame extends State
case object Lost extends State
case object Won extends State
