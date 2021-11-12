// The instruction and data memory modules

package memory

import chisel3._
import memory.MemoryOperation._

/**
  * This is the actual memory. You should never directly use this in the CPU.
  * This module should only be instantiated in the Top file.
  *
  * The I/O for this module is defined in [[MemPortBusIO]].
  */

class DualPortedCombinMemory(size: Int, memfile: String) extends BaseDualPortedMemory (size, memfile) {
  def wireMemPipe(portio: MemPortBusIO): Unit = {
    portio.response.valid := false.B
    // Combinational memory is inherently always ready for port requests
    portio.request.ready := true.B
  }
  // Instruction port
  val r1 = RegInit(0.U)
 // val r2  = RegNext(io.rx_data)

  wireMemPipe(io.imem)

  when (io.imem.request.valid) {
    // Put the Request into the instruction pipe and signal that instruction memory is busy
    val request = io.imem.request.bits

    // We should only be expecting a read from instruction memory
    assert(request.operation === Read)
    // Check that address is pointing to a valid location in memory

    // TODO: Revert this back to the assert form "assert (request.address < size.U)"
    // TODO: once CSR is integrated into CPU
   //PAUL
    // when (request.address < size.U) {
      io.imem.response.valid := true.B
      io.imem.response.bits.data := memory(request.address >> 2)
    //} .otherwise {
     // io.imem.response.valid := false.B
   // }
  } .otherwise {
    io.imem.response.valid := false.B
  }

  // Data port

  wireMemPipe(io.dmem)

  val memAddress = io.dmem.request.bits.address
  val memWriteData = io.dmem.request.bits.writedata

  when (io.dmem.request.valid) {
    //printf("memaddress %x\n",memAddress)
    val request = io.dmem.request.bits

    // Check that non-combin write isn't being used
    assert (request.operation =/= Write)
    // Check that address is pointing to a valid location in memory
    //assert (request.address < size.U)
    io.rx_data_done := r1
    // Read path
    when ( memAddress === 0x20000008.U) {
      printf("read path as well\n")
    }
    when ( memAddress === 0x20000000.U) {
      io.dmem.response.bits.data := io.rx_data
     // printf("read data %d",io.rx_data)
    }.elsewhen ( memAddress === 0x20000004.U) {
      io.dmem.response.bits.data := io.rx_rstrb
    }.otherwise {
      io.dmem.response.bits.data := memory.read(memAddress >> 2)
    }
    io.dmem.response.valid := true.B
    r1 := 0.U
    // Write path
    when (request.operation === ReadWrite) {
      when (memAddress === 0x20000008.U) {
        printf("write to 200 %d\n", memWriteData)
      }.elsewhen(memAddress === 0x20000010.U) {
        r1 := memWriteData
      }.otherwise {
        memory(memAddress >> 2) := memWriteData
      }
    }




  } .otherwise {
    io.dmem.response.valid := false.B
  }
}