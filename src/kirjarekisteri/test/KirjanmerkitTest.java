package kirjarekisteri.test;
// Generated by ComTest BEGIN
import kirjarekisteri.*;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2022.04.04 19:38:06 // Generated by ComTest
 *
 */
@SuppressWarnings("all")
public class KirjanmerkitTest {


  // Generated by ComTest BEGIN
  /** 
   * testLueTiedostosta80 
   * @throws SailoException when error
   */
  @Test
  public void testLueTiedostosta80() throws SailoException {    // Kirjanmerkit: 80
    Kirjanmerkit merkit = new Kirjanmerkit(); 
    Kirjanmerkki tennis1 = new Kirjanmerkki(); tennis1.taytaTiedoillaTennisMerkki(2); 
    Kirjanmerkki tennis2 = new Kirjanmerkki(); tennis2.taytaTiedoillaTennisMerkki(1); 
    Kirjanmerkki tennis3 = new Kirjanmerkki(); tennis3.taytaTiedoillaTennisMerkki(2); 
    Kirjanmerkki tennis4 = new Kirjanmerkki(); tennis4.taytaTiedoillaTennisMerkki(1); 
    Kirjanmerkki tennis5 = new Kirjanmerkki(); tennis5.taytaTiedoillaTennisMerkki(2); 
    String hakemisto = "testikelmit"; 
    File dir = new File(hakemisto); 
    dir.mkdir(); 
    dir.delete(); 
    try {
    merkit.lueTiedostosta(hakemisto); 
    fail("Kirjanmerkit: 96 Did not throw SailoException");
    } catch(SailoException _e_){ _e_.getMessage(); }
    merkit.lisaa(tennis1); 
    merkit.lisaa(tennis2); 
    merkit.lisaa(tennis3); 
    merkit.lisaa(tennis4); 
    merkit.lisaa(tennis5); 
    try {
    merkit.tallenna(hakemisto); 
    fail("Kirjanmerkit: 102 Did not throw SailoException");
    } catch(SailoException _e_){ _e_.getMessage(); }
    merkit = new Kirjanmerkit(); 
    merkit.lueTiedostosta(hakemisto); 
    Iterator<Kirjanmerkki> i = merkit.iterator(); 
    assertEquals("From: Kirjanmerkit line: 106", tennis1.toString(), i.next().toString()); 
    assertEquals("From: Kirjanmerkit line: 107", tennis2.toString(), i.next().toString()); 
    assertEquals("From: Kirjanmerkit line: 108", tennis3.toString(), i.next().toString()); 
    assertEquals("From: Kirjanmerkit line: 109", tennis4.toString(), i.next().toString()); 
    assertEquals("From: Kirjanmerkit line: 110", tennis5.toString(), i.next().toString()); 
    assertEquals("From: Kirjanmerkit line: 111", false, i.hasNext()); 
    merkit.lisaa(tennis5); 
    merkit.tallenna(hakemisto); 
    assertEquals("From: Kirjanmerkit line: 114", true, dir.delete()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testPoista153 
   * @throws SailoException when error
   */
  @Test
  public void testPoista153() throws SailoException {    // Kirjanmerkit: 153
    Kirjanmerkit merkit = new Kirjanmerkit(); 
    Kirjanmerkki pitsi21 = new Kirjanmerkki(); pitsi21.taytaTiedoillaTennisMerkki(2); 
    Kirjanmerkki pitsi11 = new Kirjanmerkki(); pitsi11.taytaTiedoillaTennisMerkki(1); 
    Kirjanmerkki pitsi22 = new Kirjanmerkki(); pitsi22.taytaTiedoillaTennisMerkki(2); 
    Kirjanmerkki pitsi12 = new Kirjanmerkki(); pitsi12.taytaTiedoillaTennisMerkki(1); 
    Kirjanmerkki pitsi23 = new Kirjanmerkki(); pitsi23.taytaTiedoillaTennisMerkki(2); 
    merkit.lisaa(pitsi21); 
    merkit.lisaa(pitsi11); 
    merkit.lisaa(pitsi22); 
    merkit.lisaa(pitsi12); 
    assertEquals("From: Kirjanmerkit line: 167", false, merkit.poista(pitsi23)); assertEquals("From: Kirjanmerkit line: 167", 4, merkit.getLkm()); 
    assertEquals("From: Kirjanmerkit line: 168", true, merkit.poista(pitsi11)); assertEquals("From: Kirjanmerkit line: 168", 3, merkit.getLkm()); 
    List<Kirjanmerkki> h = merkit.annaKirjanmerkit(1); 
    assertEquals("From: Kirjanmerkit line: 170", 1, h.size()); 
    assertEquals("From: Kirjanmerkit line: 171", pitsi12, h.get(0)); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testPoistaKirjanKirjanmerkit185 */
  @Test
  public void testPoistaKirjanKirjanmerkit185() {    // Kirjanmerkit: 185
    Kirjanmerkit merkit = new Kirjanmerkit(); 
    Kirjanmerkki pitsi21 = new Kirjanmerkki(); pitsi21.taytaTiedoillaTennisMerkki(2); 
    Kirjanmerkki pitsi11 = new Kirjanmerkki(); pitsi11.taytaTiedoillaTennisMerkki(1); 
    Kirjanmerkki pitsi22 = new Kirjanmerkki(); pitsi22.taytaTiedoillaTennisMerkki(2); 
    Kirjanmerkki pitsi12 = new Kirjanmerkki(); pitsi12.taytaTiedoillaTennisMerkki(1); 
    Kirjanmerkki pitsi23 = new Kirjanmerkki(); pitsi23.taytaTiedoillaTennisMerkki(2); 
    merkit.lisaa(pitsi21); 
    merkit.lisaa(pitsi11); 
    merkit.lisaa(pitsi22); 
    merkit.lisaa(pitsi12); 
    merkit.lisaa(pitsi23); 
    assertEquals("From: Kirjanmerkit line: 198", 3, merkit.poistaKirjanKirjanmerkit(2)); assertEquals("From: Kirjanmerkit line: 198", 2, merkit.getLkm()); 
    assertEquals("From: Kirjanmerkit line: 199", 0, merkit.poistaKirjanKirjanmerkit(3)); assertEquals("From: Kirjanmerkit line: 199", 2, merkit.getLkm()); 
    List<Kirjanmerkki> h = merkit.annaKirjanmerkit(2); 
    assertEquals("From: Kirjanmerkit line: 201", 0, h.size()); 
    h = merkit.annaKirjanmerkit(1); 
    assertEquals("From: Kirjanmerkit line: 203", pitsi11, h.get(0)); 
    assertEquals("From: Kirjanmerkit line: 204", pitsi12, h.get(1)); 
  } // Generated by ComTest END
}