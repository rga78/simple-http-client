

#A _Very_ Simple Http Client

Features a fluent API. Wraps around HttpURLConnection. No other dependencies.


##For example

Send a "GET" request to "http://myhost:8080/my/uri/path" and return the response as a `List<String>` (one per line):

    List<String> response = new SimpleHttpClient().setTarget( "http://myhost:8080" ) 
                                                  .path("my/uri")
                                                  .path("path")
                                                  .header( "Accept", "text/plain" )
                                                  .get()
                                                  .readEntity( new StringEntityReader() );



##Build and test

It's a maven project, so...

    $ mvn package 

