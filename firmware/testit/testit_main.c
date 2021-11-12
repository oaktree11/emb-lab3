// See LICENSE for license details.

//**************************************************************************
// Median filter bencmark
//--------------------------------------------------------------------------
//
// This benchmark performs a 1D three element median filter. The
// input data (and reference data) should be generated using the
// median_gendata.pl perl script and dumped to a file named
// dataset1.h.


__sync_fetch_and_add_4(){
}
malloc(){}
abort(){}
int main( int argc, char* argv[] )
{
  int * out = (int *)0x20000008;
  
  int * data = (int *)0x20000000;
  int * strobe = (int*)0x20000004;
  int * datadone = (int*)0x20000010;
  
  *out = 9;
  int y = *data;
  *out = y+10;
  
  while (*strobe){
      int ch = *data;
      *out = ch;
      *datadone = 1;
      } 
  if (*strobe){
      int ch = *data;
      *out = ch;
      *datadone = 1;
      }     
  int *p = (int *)0x30000000;
  *p = 56789;
}
