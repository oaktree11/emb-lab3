package CPU

import Chisel.fromtIntToLiteral
import chisel3._//{Hexadecimal, PrintableHelper}
import chisel3.iotesters.{Driver, PeekPokeTester}
import utils.CPUConfig

import chisel3.util.experimental.loadMemoryFromFile


class DebugTests(c: Tile) extends PeekPokeTester(c) {
  var myList = Array(1, 5, 3, 3,4,66,88)
  var cnt=0
 do {
   if (cnt < myList.length) {
     poke(c.io.rx_data, myList(cnt))
     poke(c.io.rx_rstrb, 1)
   }
   else {
     poke(c.io.rx_rstrb, 0)
   }
   step(1)
   if (peek(c.io.rx_data_done) == 1) {
      cnt = cnt + 1
   }
   // println("in top test_passed %d" + peek(c.io.test_passed))

 } while(peek(c.io.test_passed)!= 1)
}

object Testing {
  def main(args: Array[String]): Unit = {

    val conf = new CPUConfig()
    // Convert the binary to a hex file that can be loaded by treadle
    // (Do this after compiling the firrtl so the directory is created)

    conf.cpuType     = "single-cycle"
    conf.memFile     = "firmware/firmware.hex"
    conf.memType     = "combinational"
    conf.memPortType = "combinational-port"
    conf.memLatency  = 0
    if (!Driver(() => new Tile(conf),"treadle")(c => new DebugTests(c))) System.exit(1)
  }
}
