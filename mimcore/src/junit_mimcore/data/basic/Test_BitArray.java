package junit_mimcore.data.basic;

import mimcore.data.BitArray.BitArray;
import mimcore.data.BitArray.BitArrayBuilder;

import static org.junit.Assert.*;
import org.junit.Test;



public class Test_BitArray {

	@Test
	public void test_b1() {
		BitArrayBuilder c=new BitArrayBuilder(8);
		c.setBit(0);
		BitArray b=c.getBitArray();
		
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
	public void test_ms1() {
		BitArrayBuilder c=new BitArrayBuilder(8);
		c.setBit(0);
		c.setBit(0);
		BitArray b=c.getBitArray();
		
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
	public void test_b2() {
		BitArrayBuilder c=new BitArrayBuilder(8);
		c.setBit(1);
		BitArray b=c.getBitArray();

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
	public void test_b3() {
		BitArrayBuilder c=new BitArrayBuilder(8);
		c.setBit(2);
		BitArray b=c.getBitArray();
		
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
	public void test_b4() {
		BitArrayBuilder c=new BitArrayBuilder(8);
		c.setBit(3);
		BitArray b=c.getBitArray();
		
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
	public void test_b5() {
		BitArrayBuilder c=new BitArrayBuilder(8);
		c.setBit(4);
		BitArray b=c.getBitArray();
		
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
	public void test_b6() {
		BitArrayBuilder c=new BitArrayBuilder(8);
		c.setBit(5);
		BitArray b=c.getBitArray();
		
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
	public void test_b7() {
		BitArrayBuilder c=new BitArrayBuilder(8);
		c.setBit(6);
		BitArray b=c.getBitArray();
		
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
	public void test_b8() {
		BitArrayBuilder c=new BitArrayBuilder(8);
		c.setBit(7);
		BitArray b=c.getBitArray();
		
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
	public void test_b9() {
		BitArrayBuilder c=new BitArrayBuilder(9);
		c.setBit(8);
		BitArray b=c.getBitArray();
		
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
	public void test_b10() {
		BitArrayBuilder c=new BitArrayBuilder(10);
		c.setBit(9);
		BitArray b=c.getBitArray();
		
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
	public void test_immutable() {
		BitArrayBuilder c=new BitArrayBuilder(8);
		c.setBit(1);
		BitArray b=c.getBitArray();
		c.setBit(2);
		BitArray d=c.getBitArray();
		
		assertFalse(b.hasBit(0));
		assertTrue(b.hasBit(1));
		assertFalse(b.hasBit(2));
		assertFalse(b.hasBit(3));
		assertFalse(b.hasBit(4));
		assertFalse(b.hasBit(5));
		assertFalse(b.hasBit(6));
		assertFalse(b.hasBit(7));
		assertFalse(d.hasBit(0));
		assertTrue(d.hasBit(1));
		assertTrue(d.hasBit(2));
		assertFalse(d.hasBit(3));
	}

}
