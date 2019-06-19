public abstract class LexArithArray extends IO{

	public static String keyword = "";				//holds keyword to match
    
	public enum State { 
	  // non-final states     ordinal number
	  
		Start,
        Bar,              	//1
        Ampersand,          //2
		Period,    			//3
		E,					//4
        EPlusMinus,			//5 

      // final states

        Id,                 //7

            // keyword final states

            Keyword_if,            	//8
            Keyword_else,          	//9
            Keyword_returnVal,    	//10
            Keyword_new,            //11
            Keyword_print,         	//12

        Add,                //13
        Sub,                //14
        Mul,                //15
        Div,             	//16
		LParen,             //17         
		RParen,             //18
        LBrace,             //19
        RBrace,            	//20
        LBracket,          	//21
        RBracket,           //22
        Semicolon,         	//23
		Comma,             	//24
		Or,					//25
        And,                //26
        Int,                //27
        Float,             	//28
        FloatE,             //29
        Inv,              	//30
		Neq,               	//31
		Assign,				//32
		Eq,					//33
		Lt,					//34
		Le,					//35
		Gt,					//36
		Ge,					//37

		UNDEF;

		private boolean isFinal()
		{
			return ( this.compareTo(State.Id) >= 0 );  
		}	
	}
	// By enumerating the non-final states first and then the final states,
	// test for a final state can be done by testing if the state's ordinal number
	// is greater than or equal to that of Id.

	// The following variables of "IO" class are used:

	//   static int a; the current input character
	//   static char c; used to convert the variable "a" to the char type whenever necessary

	public static String t; // holds an extracted token
	public static State state; // the current state of the FA

	private static int driver(){

		// This is the driver of the FA. 
		// If a valid token is found, assigns it to "t" and returns 1.
		// If an invalid token is found, assigns it to "t" and returns 0.
		// If end-of-stream is reached without finding any non-whitespace character, returns -1.

		State nextSt; // the next state of the FA

		t = "";
		state = State.Start;

		if ( Character.isWhitespace((char) a) )
			a = getChar(); // get the next non-whitespace character
		if ( a == -1 ) // end-of-stream is reached
			return -1;

		while ( a != -1 ) // do the body if "a" is not end-of-stream
		{
			c = (char) a;
			nextSt = nextState( state, c, keyword );
			if ( nextSt == State.UNDEF ) // The FA will halt.
			{
				if ( state.isFinal() ){
					return 1; // valid token extracted
				}
				else // "c" is an unexpected character
				{
					t = t+c;
					a = getNextChar();
					return 0; // invalid token found
				}
			}
			else // The FA will go on.
			{
				state = nextSt;
				t = t+c;
				a = getNextChar();
			}
		}

		// end-of-stream is reached while a token is being extracted

		if ( state.isFinal() )
			return 1; // valid token extracted
		else
			return 0; // invalid token found
	} // end driver

	public static void getToken(){

	// Extract the next token using the driver of the FA.
	// If an invalid token is found, issue an error message.

		int i = driver();
		if ( i == 0 )
			displayln(t + " : Lexical Error, invalid token");
	}

	/* Could not store the individual tokens to compare to keywords because this is a static method. 
	 Static methods are unable to call static variables, to store the character token to compare to the keywords*/
	private static State nextState(State s, char c, String keyword){

	// Returns the next state of the FA given the current state and input char;
	// if the next state is undefined, UNDEF is returned.

		switch( state ){
		
		case Start:
			if ( Character.isLetter(c)){
				return State.Id;
			}
			else if ( Character.isDigit(c) )
				return State.Int;
			else if ( c == '+' )
				return State.Add;
			else if ( c == '-' )
                return State.Sub;
			else if ( c == '*' )
				return State.Mul;
			else if ( c == '/' )
				return State.Div;
			else if ( c == '(' )
				return State.LParen;
			else if ( c == ')' )
				return State.RParen;
			else if ( c == '{' )
				return State.LBrace;
			else if ( c == '}' )
				return State.RBrace;
			else if ( c == '[' )
				return State.LBracket;
			else if ( c == ']' )
				return State.RBracket;
            else if ( c == ';' )
                return State.Semicolon;
             else if ( c == ',' )
                return State.Comma;
            else if ( c == '|' )
                return State.Bar;
            else if ( c == '&' )
				return State.Ampersand;
			else if ( c == '!' )
				return State.Inv;
			else if ( c == '=' )
				return State.Assign;
			else if ( c == '<' )
				return State.Lt;
			else if ( c == '>' )
				return State.Gt;
			
			else
				return State.UNDEF;
		case Id:
			//in the id letter case we will check for a keyword
			if ( Character.isLetter(c) )
				return State.Id;
			else if ( Character.isDigit(c) )
				return State.Id;
			else
				return State.UNDEF;
		case Int:
			if ( Character.isDigit(c) )
				return State.Int;
			else if ( c == '.' )
				return State.Period;
			else
				return State.UNDEF;
		case Period:
			if ( Character.isDigit(c) )
				return State.Float;
			else
				return State.UNDEF;
		case Float:
			if ( Character.isDigit(c) )
				return State.Float;
			else if ( c == 'e' || c == 'E' )
				return State.E;
			else
				return State.UNDEF;
		case E:
			if ( Character.isDigit(c) )
				return State.FloatE;
			else if ( c == '+' || c == '-' )
				return State.EPlusMinus;
			else
				return State.UNDEF;
		case EPlusMinus:
			if ( Character.isDigit(c) )
				return State.FloatE;
			else
				return State.UNDEF;
		case FloatE:
			if ( Character.isDigit(c) )
				return State.FloatE;
			else
                return State.UNDEF;
        case Bar:
            if ( c == '|' )
                return State.Or;
            else
				return State.UNDEF;
		case Ampersand:
			if ( c == '&' )
				return State.And;
			else
				return State.UNDEF;
		case Inv:
			if ( c == '=' )
				return State.Neq;
			else
				return State.UNDEF;
		case Assign:
			if ( c == '=' )
				return State.Eq;
			else
				return State.UNDEF;
		case Lt:
			if ( c == '=' )
				return State.Le;
			else
				return State.UNDEF;
		case Gt:
			if ( c == '=' )
				return State.Ge;
			else
				return State.UNDEF;
		default:
			return State.UNDEF;
		}
	} // end nextState

	public static void main(String argv[]){		
		// argv[0]: input file containing source code using tokens defined above
		// argv[1]: output file displaying a list of the tokens 
		// state shift will be done during the output phase below of the program

		// array to hold keywords
		String keywords[] = {
			"if",
			"else",
			"while",
			"returnVal",
			"new",
			"print",
		};

		setIO( argv[0], argv[1] );
		
		int i;

		while ( a != -1 ) // while "a" is not end-of-stream
		{
			boolean state_shift = true;		// state is shifted if token matches a keyword
			i = driver(); 					// extract the next token
			if ( i == 1 ){
				//if token is a keyword, the appropriate state is printed instead
				for(int j=0; j < 6; j++){
					if(t.equals(keywords[j])){
						displayln( t+"   : Keyword_" + keywords[j] );
						state_shift = false;
					}
				}
				// if token is not a keyword original id state is printed
				if(state_shift == true)
					displayln( t+"   : "+state.toString() );
			}
			else if ( i == 0 )
				displayln( t+" : Lexical Error, invalid token");
		} 

		closeIO();
	}
} 