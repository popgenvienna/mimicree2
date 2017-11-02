package junit_mimcore.data.basic;

import mimcore.data.BitArray.BitArrayBuilder;
import static org.junit.Assert.*;
import org.junit.Test;
public class Test_BitArrayBuilder {

	@Test
	public void set_first_bit() {
		BitArrayBuilder b=new BitArrayBuilder(8);
		b.setBit(0);
		assertTrue(b.hasBit(0));
		assertFalse(b.hasBit(1));
		assertFalse(b.hasBit(2));
		assertFalse(b.hasBit(3));
		assertFalse(b.hasBit(4));
		assertFalse(b.hasBit(5));
		assertFalse(b.hasBit(6));
		assertFalse(b.hasBit(7));
	}
	
	@Test
	public void second_bit_set_twice() {
		BitArrayBuilder b=new BitArrayBuilder(8);
		b.setBit(0);
		b.setBit(0);
		assertTrue(b.hasBit(0));
		assertFalse(b.hasBit(1));
		assertFalse(b.hasBit(2));
		assertFalse(b.hasBit(3));
		assertFalse(b.hasBit(4));
		assertFalse(b.hasBit(5));
		assertFalse(b.hasBit(6));
		assertFalse(b.hasBit(7));
	}
	
	@Test
	public void set_second_bit() {
		BitArrayBuilder b=new BitArrayBuilder(8);
		b.setBit(1);
		assertFalse(b.hasBit(0));
		assertTrue(b.hasBit(1));
		assertFalse(b.hasBit(2));
		assertFalse(b.hasBit(3));
		assertFalse(b.hasBit(4));
		assertFalse(b.hasBit(5));
		assertFalse(b.hasBit(6));
		assertFalse(b.hasBit(7));
	}
	
	@Test
	public void set_third_bit() {
		BitArrayBuilder b=new BitArrayBuilder(8);
		b.setBit(2);
		assertFalse(b.hasBit(0));
		assertFalse(b.hasBit(1));
		assertTrue(b.hasBit(2));
		assertFalse(b.hasBit(3));
		assertFalse(b.hasBit(4));
		assertFalse(b.hasBit(5));
		assertFalse(b.hasBit(6));
		assertFalse(b.hasBit(7));
	}
	
	@Test
	public void set_fourth_bit() {
		BitArrayBuilder b=new BitArrayBuilder(8);
		b.setBit(3);
		assertFalse(b.hasBit(0));
		assertFalse(b.hasBit(1));
		assertFalse(b.hasBit(2));
		assertTrue(b.hasBit(3));
		assertFalse(b.hasBit(4));
		assertFalse(b.hasBit(5));
		assertFalse(b.hasBit(6));
		assertFalse(b.hasBit(7));
	}
	
	@Test
	public void set_fifth_bit() {
		BitArrayBuilder b=new BitArrayBuilder(8);
		b.setBit(4);
		assertFalse(b.hasBit(0));
		assertFalse(b.hasBit(1));
		assertFalse(b.hasBit(2));
		assertFalse(b.hasBit(3));
		assertTrue(b.hasBit(4));
		assertFalse(b.hasBit(5));
		assertFalse(b.hasBit(6));
		assertFalse(b.hasBit(7));
	}
	
	@Test
	public void set_sixth_bit() {
		BitArrayBuilder b=new BitArrayBuilder(8);
		b.setBit(5);
		assertFalse(b.hasBit(0));
		assertFalse(b.hasBit(1));
		assertFalse(b.hasBit(2));
		assertFalse(b.hasBit(3));
		assertFalse(b.hasBit(4));
		assertTrue(b.hasBit(5));
		assertFalse(b.hasBit(6));
		assertFalse(b.hasBit(7));
	}
	
	@Test
	public void set_seventh_bit() {
		BitArrayBuilder b=new BitArrayBuilder(8);
		b.setBit(6);
		assertFalse(b.hasBit(0));
		assertFalse(b.hasBit(1));
		assertFalse(b.hasBit(2));
		assertFalse(b.hasBit(3));
		assertFalse(b.hasBit(4));
		assertFalse(b.hasBit(5));
		assertTrue(b.hasBit(6));
		assertFalse(b.hasBit(7));
	}
	
	@Test
	public void set_eighth_bit() {
		BitArrayBuilder b=new BitArrayBuilder(8);
		b.setBit(7);
		assertFalse(b.hasBit(0));
		assertFalse(b.hasBit(1));
		assertFalse(b.hasBit(2));
		assertFalse(b.hasBit(3));
		assertFalse(b.hasBit(4));
		assertFalse(b.hasBit(5));
		assertFalse(b.hasBit(6));
		assertTrue(b.hasBit(7));
	}
	
	@Test
	public void set_ninth_bit() {
		BitArrayBuilder b=new BitArrayBuilder(9);
		b.setBit(8);
		assertFalse(b.hasBit(0));
		assertFalse(b.hasBit(1));
		assertFalse(b.hasBit(2));
		assertFalse(b.hasBit(3));
		assertFalse(b.hasBit(4));
		assertFalse(b.hasBit(5));
		assertFalse(b.hasBit(6));
		assertFalse(b.hasBit(7));
		assertTrue(b.hasBit(8));
	}
	
	@Test
	public void set_tenth_bit() {
		BitArrayBuilder b=new BitArrayBuilder(10);
		b.setBit(9);
		assertFalse(b.hasBit(0));
		assertFalse(b.hasBit(1));
		assertFalse(b.hasBit(2));
		assertFalse(b.hasBit(3));
		assertFalse(b.hasBit(4));
		assertFalse(b.hasBit(5));
		assertFalse(b.hasBit(6));
		assertFalse(b.hasBit(7));
		assertFalse(b.hasBit(8));
		assertTrue(b.hasBit(9));
	}
	
	@Test
	public void no_bit_set() {
		BitArrayBuilder b=new BitArrayBuilder(8);
		assertFalse(b.hasBit(0));
		assertFalse(b.hasBit(1));
		assertFalse(b.hasBit(2));
		assertFalse(b.hasBit(3));
		assertFalse(b.hasBit(4));
		assertFalse(b.hasBit(5));
		assertFalse(b.hasBit(6));
		assertFalse(b.hasBit(7));
	}
	
	@Test
	public void all_bits_set() {
		BitArrayBuilder b=new BitArrayBuilder(12);
		b.setBit(0); b.setBit(1); b.setBit(2);b.setBit(3); b.setBit(4); b.setBit(5); b.setBit(6); b.setBit(7); b.setBit(8); b.setBit(9); b.setBit(10); b.setBit(11);		
		assertTrue(b.hasBit(0));
		assertTrue(b.hasBit(1));
		assertTrue(b.hasBit(2));
		assertTrue(b.hasBit(3));
		assertTrue(b.hasBit(4));
		assertTrue(b.hasBit(5));
		assertTrue(b.hasBit(6));
		assertTrue(b.hasBit(7));
		assertTrue(b.hasBit(8));
		assertTrue(b.hasBit(9));
		assertTrue(b.hasBit(10));
		assertTrue(b.hasBit(11));
	}
	
	@Test
	public void some_bis_set_some_bits_unset_combination1() {
		BitArrayBuilder b=new BitArrayBuilder(8);
		b.setBit(0);
		b.setBit(7);
		assertTrue(b.hasBit(0));
		assertFalse(b.hasBit(1));
		assertFalse(b.hasBit(2));
		assertFalse(b.hasBit(3));
		assertFalse(b.hasBit(4));
		assertFalse(b.hasBit(5));
		assertFalse(b.hasBit(6));
		assertTrue(b.hasBit(7));
	}
	
	@Test
	public void some_bis_set_some_bits_unset_combination2() {
		BitArrayBuilder b=new BitArrayBuilder(8);
		b.setBit(1);
		b.setBit(2);
		assertFalse(b.hasBit(0));
		assertTrue(b.hasBit(1));
		assertTrue(b.hasBit(2));
		assertFalse(b.hasBit(3));
		assertFalse(b.hasBit(4));
		assertFalse(b.hasBit(5));
		assertFalse(b.hasBit(6));
		assertFalse(b.hasBit(7));
	}
	
	@Test
	public void some_bis_set_some_bits_unset_combination3() {
		BitArrayBuilder b=new BitArrayBuilder(8);
		b.setBit(0);
		b.setBit(4);
		assertTrue(b.hasBit(0));
		assertFalse(b.hasBit(1));
		assertFalse(b.hasBit(2));
		assertFalse(b.hasBit(3));
		assertTrue(b.hasBit(4));
		assertFalse(b.hasBit(5));
		assertFalse(b.hasBit(6));
		assertFalse(b.hasBit(7));
	}
	
	@Test
	public void some_bis_set_some_bits_unset_combination4() {
		BitArrayBuilder b=new BitArrayBuilder(8);
		b.setBit(2);
		b.setBit(6);
		b.setBit(4);
		assertFalse(b.hasBit(0));
		assertFalse(b.hasBit(1));
		assertTrue(b.hasBit(2));
		assertFalse(b.hasBit(3));
		assertTrue(b.hasBit(4));
		assertFalse(b.hasBit(5));
		assertTrue(b.hasBit(6));
		assertFalse(b.hasBit(7));
	}
	
	
	@Test
	public void set_bit_with_high_index_72() {
		BitArrayBuilder b=new BitArrayBuilder(80);
		b.setBit(72);
		assertTrue(b.hasBit(72));
		assertFalse(b.hasBit(73));
		assertFalse(b.hasBit(74));
		assertFalse(b.hasBit(75));
		assertFalse(b.hasBit(76));
		assertFalse(b.hasBit(77));
		assertFalse(b.hasBit(78));
		assertFalse(b.hasBit(79));
	}
	
	@Test
	public void set_bit_with_high_index_73() {
		BitArrayBuilder b=new BitArrayBuilder(80);
		b.setBit(73);
		assertFalse(b.hasBit(72));
		assertTrue(b.hasBit(73));
		assertFalse(b.hasBit(74));
		assertFalse(b.hasBit(75));
		assertFalse(b.hasBit(76));
		assertFalse(b.hasBit(77));
		assertFalse(b.hasBit(78));
		assertFalse(b.hasBit(79));
	}
	
	@Test
	public void set_bit_with_high_index_74() {
		BitArrayBuilder b=new BitArrayBuilder(80);
		b.setBit(74);
		assertFalse(b.hasBit(72));
		assertFalse(b.hasBit(73));
		assertTrue(b.hasBit(74));
		assertFalse(b.hasBit(75));
		assertFalse(b.hasBit(76));
		assertFalse(b.hasBit(77));
		assertFalse(b.hasBit(78));
		assertFalse(b.hasBit(79));
	}
	
	@Test
	public void set_bit_with_high_index_75() {
		BitArrayBuilder b=new BitArrayBuilder(80);
		b.setBit(75);
		assertFalse(b.hasBit(72));
		assertFalse(b.hasBit(73));
		assertFalse(b.hasBit(74));
		assertTrue(b.hasBit(75));
		assertFalse(b.hasBit(76));
		assertFalse(b.hasBit(77));
		assertFalse(b.hasBit(78));
		assertFalse(b.hasBit(79));
	}
	
	@Test
	public void set_bit_with_high_index_76() {
		BitArrayBuilder b=new BitArrayBuilder(80);
		b.setBit(76);
		assertFalse(b.hasBit(72));
		assertFalse(b.hasBit(73));
		assertFalse(b.hasBit(74));
		assertFalse(b.hasBit(75));
		assertTrue(b.hasBit(76));
		assertFalse(b.hasBit(77));
		assertFalse(b.hasBit(78));
		assertFalse(b.hasBit(79));
	}
	
	@Test
	public void set_bit_with_high_index_77() {
		BitArrayBuilder b=new BitArrayBuilder(80);
		b.setBit(77);
		assertFalse(b.hasBit(72));
		assertFalse(b.hasBit(73));
		assertFalse(b.hasBit(74));
		assertFalse(b.hasBit(75));
		assertFalse(b.hasBit(76));
		assertTrue(b.hasBit(77));
		assertFalse(b.hasBit(78));
		assertFalse(b.hasBit(79));
	}
	
	@Test
	public void set_bit_with_high_index_78() {
		BitArrayBuilder b=new BitArrayBuilder(80);
		b.setBit(78);
		assertFalse(b.hasBit(72));
		assertFalse(b.hasBit(73));
		assertFalse(b.hasBit(74));
		assertFalse(b.hasBit(75));
		assertFalse(b.hasBit(76));
		assertFalse(b.hasBit(77));
		assertTrue(b.hasBit(78));
		assertFalse(b.hasBit(79));
	}
	
	@Test
	public void set_bit_with_high_index_79() {
		BitArrayBuilder b=new BitArrayBuilder(80);
		b.setBit(79);
		assertFalse(b.hasBit(72));
		assertFalse(b.hasBit(73));
		assertFalse(b.hasBit(74));
		assertFalse(b.hasBit(75));
		assertFalse(b.hasBit(76));
		assertFalse(b.hasBit(77));
		assertFalse(b.hasBit(78));
		assertTrue(b.hasBit(79));
	}
	
	@Test
	public void test_highBitZero() {
		BitArrayBuilder b=new BitArrayBuilder(80);
		assertFalse(b.hasBit(72));
		assertFalse(b.hasBit(73));
		assertFalse(b.hasBit(74));
		assertFalse(b.hasBit(75));
		assertFalse(b.hasBit(76));
		assertFalse(b.hasBit(77));
		assertFalse(b.hasBit(78));
		assertFalse(b.hasBit(79));
	}
	
	
	@Test
	public void many_bits_with_high_index_set() {
		BitArrayBuilder b=new BitArrayBuilder(80);
		b.setBit(72); b.setBit(73); b.setBit(74); b.setBit(75); b.setBit(76); b.setBit(77); b.setBit(78); b.setBit(79);
		assertTrue(b.hasBit(72));
		assertTrue(b.hasBit(73));
		assertTrue(b.hasBit(74));
		assertTrue(b.hasBit(75));
		assertTrue(b.hasBit(76));
		assertTrue(b.hasBit(77));
		assertTrue(b.hasBit(78));
		assertTrue(b.hasBit(79));
	}
	
	
	@Test
	public void combination_of_bits_with_high_index() {
		BitArrayBuilder b=new BitArrayBuilder(80);
		b.setBit(0); b.setBit(10); b.setBit(20); b.setBit(30); b.setBit(40); b.setBit(50); b.setBit(60); b.setBit(70);
		assertTrue(b.hasBit(0));
		assertTrue(b.hasBit(10));
		assertTrue(b.hasBit(20));
		assertTrue(b.hasBit(30));
		assertTrue(b.hasBit(40));
		assertTrue(b.hasBit(50));
		assertTrue(b.hasBit(60));
		assertTrue(b.hasBit(70));
		
	}

	
}
