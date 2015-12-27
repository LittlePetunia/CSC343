import java.util.ArrayList;
import java.sql.*;
import java.util.Collections;

public class Assignment2 {

	/* A connection to the database */
	private Connection connection;

	/**
	 * Empty constructor. There is no need to modify this. 
	 */
	public Assignment2() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Failed to find the JDBC driver");
		}
	}

	/**
	* Establishes a connection to be used for this session, assigning it to
	* the instance variable 'connection'.
	*
	* @param  url       the url to the database
	* @param  username  the username to connect to the database
	* @param  password  the password to connect to the database
	* @return           true if the connection is successful, false otherwise
	*/
	public boolean connectDB(String url, String username, String password) {
		try {
			this.connection = DriverManager.getConnection(url, username, password);
			return true;
		} catch (SQLException se) {
			System.err.println("SQL Exception. <Message>: " + se.getMessage());
			return false;
		}
	}

	/**
	* Closes the database connection.
	*
	* @return true if the closing was successful, false otherwise
	*/
	public boolean disconnectDB() {
		try {
			this.connection.close();
		return true;
		} catch (SQLException se) {
			System.err.println("SQL Exception. <Message>: " + se.getMessage());
			return false;
		}
	}

	/**
	 * Returns a sorted list of the names of all musicians and bands 
	 * who released at least one album in a given genre. 
	 *
	 * Returns an empty list if no such genre exists or no artist matches.
	 *
	 * NOTE:
	 *    Use Collections.sort() to sort the names in ascending
	 *    alphabetical order.
	 *
	 * @param genre  the genre to find artists for
	 * @return       a sorted list of artist names
	 */
	public ArrayList<String> findArtistsInGenre(String genre) {
	  ArrayList<String> artistName = new ArrayList<String>();
	  Statement sta = null;
	  ResultSet res = null;

	  String query = "select distinct name " +
	                 "from Artist,Album,Genre " +
	                 "where Artist.artist_id=Album.artist_id and Album.genre_id=Genre.genre_id and genre= '"
	                 + genre + "';";
	  try {
	  sta = connection.createStatement();
	  PreparedStatement set =connection.prepareStatement("set search_path to artistdb;");
	  set.execute();
	  res = sta.executeQuery(query);
	  while (res.next()) {artistName.add(res.getString(1));}
	  Collections.sort(artistName);
	  } catch (SQLException ex) {
	      System.err.println(ex.getMessage());
	  } finally {
	    if (sta!=null && res!=null) {
	  try {
	    sta.close();
	    res.close();
	  } catch (SQLException ex) {
	      System.out.println(ex);
	    }
	  }
	  
	  
	  return artistName;
	  }
    }


	/**
	 * Returns a sorted list of the names of all collaborators
	 * (either as a main artist or guest) for a given artist.  
	 *
	 * Returns an empty list if no such artist exists or the artist 
	 * has no collaborators.
	 *
	 * NOTE:
	 *    Use Collections.sort() to sort the names in ascending
	 *    alphabetical order.
	 *
	 * @param artist  the name of the artist to find collaborators for
	 * @return        a sorted list of artist names
	 */
	public ArrayList<String> findCollaborators(String artist) {
		
		ArrayList<String> CollabArtist = new ArrayList<String>();
		Statement sta=null;
		ResultSet res=null;
		
		String query ="(select * from Mains) UNION (select * from Cos);";
		try{
		sta = connection.createStatement();
		PreparedStatement set = connection.prepareStatement("set search_path to artistdb;");
		set.execute();
		sta.execute("create view Targe as " +
                              "select artist_id " +
                              "from Artist " +
                              "where name='" + artist + "';" );
               sta.execute("create view Mains as " +
                              "select name " +
                              "from Targe, Collaboration, Artist " +
                              "where Targe.artist_id=artist1 and Artist.artist_id=artist2;");
               sta.execute("create view Cos as " +     
                              "select name " +
                              "from Targe,Collaboration,Artist " +
                              "where Targe.artist_id=artist2 and Artist.artist_id=artist1;");
               res = sta.executeQuery(query);
               while (res.next()) {CollabArtist.add(res.getString(1));}
               
               sta.execute("Drop view Mains Cascade;");
               sta.execute("Drop view Cos Cascade;");
               sta.execute("Drop view Targe Cascade;");
               Collections.sort(CollabArtist);
               } catch (SQLException ex) {
		  System.err.println(ex.getMessage());
	       } finally {
		  if (sta!=null && res!=null) {
	       try {
		  sta.close();
		  res.close();
	       } catch (SQLException ex) {
		  System.out.println(ex);
	         }
	       }
	       return CollabArtist;
               }
        }

	/**
	 * Returns a sorted list of the names of all songwriters
	 * who wrote songs for a given artist (the given artist is excluded).  
	 *
	 * Returns an empty list if no such artist exists or the artist 
	 * has no other songwriters other than themself.
	 *
	 * NOTE:
	 *    Use Collections.sort() to sort the names in ascending
	 *    alphabetical order.
	 *
	 * @param artist  the name of the artist to find the songwriters for
	 * @return        a sorted list of songwriter names
	 */
	public ArrayList<String> findSongwriters(String artist) {
	    ArrayList<String> Songwriters = new ArrayList<String>();
	    Statement sta=null;
	    ResultSet res=null;
	    
	    String query="select distinct name " +
                           "from Wrs,Artist " +
                           "where Wrs.songwriter_id = Artist.artist_id and name !='"+artist+"';";
            try{
            sta = connection.createStatement();
	    PreparedStatement set = connection.prepareStatement("set search_path to artistdb;");
	    set.execute();
            sta.execute("create view BeWr as " +
                           "select artist_id " +
                           "from Artist " +
                           "where name='" + artist +"';");
            sta.execute("create view Wrs as " +
                           "select distinct Song.songwriter_id,song.title " +
                           "from BeWr,Song,BelongsToAlbum,Album " +
                           "where Song.song_id=BelongsToAlbum.song_id and " + 
                           "BelongsToAlbum.album_id = Album.album_id and " +
                           "Album.artist_id = BeWr.artist_id;");        
           res=sta.executeQuery(query);
           while (res.next()) {Songwriters.add(res.getString(1));}
           sta.execute("Drop view Wrs cascade;");
           sta.execute("Drop view BeWr cascade;");
           Collections.sort(Songwriters);
           } catch (SQLException ex) {
		  System.err.println(ex.getMessage());
	       } finally {
		  if (sta!=null && res!=null) {
	       try {
		  sta.close();
		  res.close();
	       } catch (SQLException ex) {
		  System.out.println(ex);
	         }
	       }
          return Songwriters;
              }
           
         
	}

	/**
	 * Returns a sorted list of the names of all acquaintances
	 * for a given pair of artists.  
	 *
	 * Returns an empty list if either of the artists does not exist, 
	 * or they have no acquaintances.
	 *
	 * NOTE:
	 *    Use Collections.sort() to sort the names in ascending
	 *    alphabetical order.
	 *
	 * @param artist1  the name of the first artist to find acquaintances for
	 * @param artist2  the name of the second artist to find acquaintances for
	 * @return         a sorted list of artist names
	 */
	public ArrayList<String> findAcquaintances(String artist1, String artist2) {
	       
               
		ArrayList<String> set1 = new ArrayList<String>();
		ArrayList<String> set2 = new ArrayList<String>();
		
		set1.addAll(findCollaborators(artist1));
		set1.addAll(findSongwriters(artist1));
		set2.addAll(findCollaborators(artist2));
		set2.addAll(findSongwriters(artist2));
		set1.retainAll(set2);
		Collections.sort(set1);
		return set1;
	}
	
	
	public static void main(String[] args) {
		
		Assignment2 a2 = new Assignment2();
		
		
		/* TODO: Change the database name and username to your own here. */
		a2.connectDB("jdbc:postgresql://localhost:5432/csc343h-g4qiao",
		             "g4qiao",
		             "");
		
                System.err.println("\n----- ArtistsInGenre -----");
                ArrayList<String> res = a2.findArtistsInGenre("Rock");
                for (String s : res) {
                  System.err.println(s);
                }

		System.err.println("\n----- Collaborators -----");
		res = a2.findCollaborators("Michael Jackson");
		for (String s : res) {
		  System.err.println(s);
		}
		
		System.err.println("\n----- Songwriters -----");
	        res = a2.findSongwriters("Justin Bieber");
		for (String s : res) {
		  System.err.println(s);
		}
		
		System.err.println("\n----- Acquaintances -----");
		res = a2.findAcquaintances("Jaden Smith", "Miley Cyrus");
		for (String s : res) {
		  System.err.println(s);
		}

		
		a2.disconnectDB();
	}
}

