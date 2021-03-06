package CPU


import CPU._
import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class ALUControlUnitRTypeTester(c: ALUControl) extends PeekPokeTester(c) {
  private val ctl = c

  // Copied from Patterson and Waterman Figure 2.3
  val tests = List(
    // alu,   itype,    Funct7,       Func3,    Control Input
    (  0.U, false.B, "b0000000".U, "b000".U, "b0010".U, "load/store"),
    (  0.U, false.B, "b1111111".U, "b111".U, "b0010".U, "load/store"),
    (  0.U, false.B, "b0000000".U, "b000".U, "b0010".U, "load/store"),
    (  2.U, false.B, "b0000000".U, "b000".U, "b0010".U, "add"),
    (  2.U, false.B, "b0100000".U, "b000".U, "b0011".U, "sub"),
    (  2.U, false.B, "b0000000".U, "b001".U, "b1001".U, "sll"),
    (  2.U, false.B, "b0000000".U, "b010".U, "b1000".U, "slt"),
    (  2.U, false.B, "b0000000".U, "b011".U, "b0101".U, "sltu"),
    (  2.U, false.B, "b0000000".U, "b100".U, "b0110".U, "xor"),
    (  2.U, false.B, "b0000000".U, "b101".U, "b0111".U, "srl"),
    (  2.U, false.B, "b0100000".U, "b101".U, "b0100".U, "sra"),
    (  2.U, false.B, "b0000000".U, "b110".U, "b0001".U, "or"),
    (  2.U, false.B, "b0000000".U, "b111".U, "b0000".U, "and"),
    (  2.U, true.B,  "b0000000".U, "b000".U, "b0010".U, "addi"),
    (  2.U, true.B,  "b0000000".U, "b010".U, "b1000".U, "slti"),
    (  2.U, true.B,  "b0000000".U, "b011".U, "b0101".U, "sltiu"),
    (  2.U, true.B,  "b0000000".U, "b100".U, "b0110".U, "xori"),
    (  2.U, true.B,  "b0000000".U, "b110".U, "b0001".U, "ori"),
    (  2.U, true.B,  "b0000000".U, "b111".U, "b0000".U, "andi"),
    (  2.U, true.B,  "b0000000".U, "b001".U, "b1001".U, "slli"),
    (  2.U, true.B,  "b0000000".U, "b101".U, "b0111".U, "srli"),
    (  2.U, true.B,  "b0100000".U, "b101".U, "b0100".U, "srai")
  )

  for (t <- tests) {
    poke(ctl.io.aluop, t._1)
    poke(ctl.io.itype, t._2)
    poke(ctl.io.funct7, t._3)
    poke(ctl.io.funct3, t._4)
    step(1)
    expect(ctl.io.operation, t._5, s"${t._6} wrong")
  }
}

/**
 * This is a trivial example of how to run this Specification
 * From within sbt use:
 * {{{
 * Lab1 / testOnly dinocpu.ALUControlTesterLab1
 * }}}
 * From a terminal shell use:
 * {{{
 * sbt 'Lab1 / testOnly dinocpu.ALUControlTesterLab1'
 * }}}
 */
class ALUControlTesterLab1 extends ChiselFlatSpec {
  "ALUControl" should s"match expectations for each intruction type" in {
    Driver(() => new ALUControl) {
      c => new ALUControlUnitRTypeTester(c)
    } should be (true)
  }
}

/**
 * This is a trivial example of how to run this Specification
 * From within sbt use:
 * {{{
 * Lab1 / testOnly dinocpu.SingleCycleAddTesterLab1
 * }}}
 * From a terminal shell use:
 * {{{
 * sbt 'Lab1 / testOnly dinocpu.SingleCycleAddTesterLab1'
 * }}}
 */
class SingleCycleAddTesterLab1 extends CPUFlatSpec {
  behavior of "Single Cycle CPU"
  var test = InstTests.nameMap("add1")
  it should s"run add test ${test.binary}${test.extraName}" in {
    CPUTesterDriver(test, "single-cycle") should be(true)
  }
  test = InstTests.nameMap("add2")
  it should s"run add test ${test.binary}${test.extraName}" in {
    CPUTesterDriver(test, "single-cycle") should be(true)
  }
}

/**
 * This is a trivial example of how to run this Specification
 * From within sbt use:
 * {{{
 * Lab1 / testOnly dinocpu.SingleCycleAdd0TesterLab1
 * }}}
 * From a terminal shell use:
 * {{{
 * sbt 'Lab1 / testOnly dinocpu.SingleCycleAdd0TesterLab1'
 * }}}
 */
class SingleCycleAdd0TesterLab1 extends CPUFlatSpec {
  val test = InstTests.nameMap("add0")
  "Single Cycle CPU" should s"run add test ${test.binary}${test.extraName}" in {
    CPUTesterDriver(test, "single-cycle") should be(true)
  }
}

class SingleCycleAdd2TesterLab1 extends CPUFlatSpec {
  val test = InstTests.nameMap("testit.riscv")
  "Single Cycle CPU" should s"run add test ${test.binary}${test.extraName}" in {
    CPUTesterDriver(test, "single-cycle") should be(true)
  }
}
/**
 * This is a trivial example of how to run this Specification
 * From within sbt use:
 * {{{
 * Lab1 / testOnly dinocpu.SingleCycleRTypeTesterLab1
 * }}}
 * From a terminal shell use:
 * {{{
 * sbt 'Lab1 / testOnly dinocpu.SingleCycleRTypeTesterLab1'
 * }}}
 *
 * To run a **single** test from this suite, you can use the -z option to sbt test.
 * The option after the `-z` is a string to search for in the test
 * {{{
 * sbt> testOnly dinocpu.SingleCycleRTypeTesterLab1 -- -z add1
 * }}}
 * Or, to run just the r-type instructions you can use `-z rtype`
 */
class SingleCycleRTypeTesterLab1 extends CPUFlatSpec {
  behavior of "Single Cycle CPU"
  for (test <- InstTests.rtype) {
    if (test.binary != "add0" && test.binary != "add1" && test.binary != "add2") {
      it should s"run R-type instruction ${test.binary}${test.extraName}" in {
        CPUTesterDriver(test, "single-cycle") should be(true)
      }
    }
  }
}

/**
 * This is a trivial example of how to run this Specification
 * From within sbt use:
 * {{{
 * Lab1 / testOnly dinocpu.SingleCycleMultiCycleTesterLab1
 * }}}
 * From a terminal shell use:
 * {{{
 * sbt 'Lab1 / testOnly dinocpu.SingleCycleMultiCycleTesterLab1'
 * }}}
 *
 * To run a **single** test from this suite, you can use the -z option to sbt test.
 * The option after the `-z` is a string to search for in the test
 * {{{
 * sbt> testOnly dinocpu.SingleCycleMultiCycleTesterLab1 -- -z swapxor
 * }}}
 * Or, to run just the r-type instructions you can use `-z rtype`
 */
class SingleCycleMultiCycleTesterLab1 extends CPUFlatSpec {
  behavior of "Single Cycle CPU"
  for (test <- InstTests.rtypeMultiCycle) {
    it should s"run R-type multi-cycle program ${test.binary}${test.extraName}" in {
      CPUTesterDriver(test, "single-cycle") should be(true)
    }
  }
}
