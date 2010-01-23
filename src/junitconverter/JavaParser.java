// $ANTLR 3.0.1 /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g 2010-01-23 22:08:44

package junitconverter;

import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import java.util.Collections;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
/** A Java 1.5 grammar for ANTLR v3 derived from the spec
 *
 *  This is a very close representation of the spec; the changes
 *  are comestic (remove left recursion) and also fixes (the spec
 *  isn't exactly perfect).  I have run this on the 1.4.2 source
 *  and some nasty looking enums from 1.5, but have not really
 *  tested for 1.5 compatibility.
 *
 *  I built this with: java -Xmx100M org.antlr.Tool java.g 
 *  and got two errors that are ok (for now):
 *  java.g:691:9: Decision can match input such as
 *    "'0'..'9'{'E', 'e'}{'+', '-'}'0'..'9'{'D', 'F', 'd', 'f'}"
 *    using multiple alternatives: 3, 4
 *  As a result, alternative(s) 4 were disabled for that input
 *  java.g:734:35: Decision can match input such as "{'$', 'A'..'Z',
 *    '_', 'a'..'z', '\u00C0'..'\u00D6', '\u00D8'..'\u00F6',
 *    '\u00F8'..'\u1FFF', '\u3040'..'\u318F', '\u3300'..'\u337F',
 *    '\u3400'..'\u3D2D', '\u4E00'..'\u9FFF', '\uF900'..'\uFAFF'}"
 *    using multiple alternatives: 1, 2
 *  As a result, alternative(s) 2 were disabled for that input
 *
 *  You can turn enum on/off as a keyword :)
 *
 *  Version 1.0 -- initial release July 5, 2006 (requires 3.0b2 or higher)
 *
 *  Primary author: Terence Parr, July 2006
 *
 *  Version 1.0.1 -- corrections by Koen Vanderkimpen & Marko van Dooren,
 *      October 25, 2006;
 *      fixed normalInterfaceDeclaration: now uses typeParameters instead
 *          of typeParameter (according to JLS, 3rd edition)
 *      fixed castExpression: no longer allows expression next to type
 *          (according to semantics in JLS, in contrast with syntax in JLS)
 *
 *  Version 1.0.2 -- Terence Parr, Nov 27, 2006
 *      java spec I built this from had some bizarre for-loop control.
 *          Looked weird and so I looked elsewhere...Yep, it's messed up.
 *          simplified.
 *
 *  Version 1.0.3 -- Chris Hogue, Feb 26, 2007
 *      Factored out an annotationName rule and used it in the annotation rule.
 *          Not sure why, but typeName wasn't recognizing references to inner
 *          annotations (e.g. @InterfaceName.InnerAnnotation())
 *      Factored out the elementValue section of an annotation reference.  Created 
 *          elementValuePair and elementValuePairs rules, then used them in the 
 *          annotation rule.  Allows it to recognize annotation references with 
 *          multiple, comma separated attributes.
 *      Updated elementValueArrayInitializer so that it allows multiple elements.
 *          (It was only allowing 0 or 1 element).
 *      Updated localVariableDeclaration to allow annotations.  Interestingly the JLS
 *          doesn't appear to indicate this is legal, but it does work as of at least
 *          JDK 1.5.0_06.
 *      Moved the Identifier portion of annotationTypeElementRest to annotationMethodRest.
 *          Because annotationConstantRest already references variableDeclarator which 
 *          has the Identifier portion in it, the parser would fail on constants in 
 *          annotation definitions because it expected two identifiers.  
 *      Added optional trailing ';' to the alternatives in annotationTypeElementRest.
 *          Wouldn't handle an inner interface that has a trailing ';'.
 *      Swapped the expression and type rule reference order in castExpression to 
 *          make it check for genericized casts first.  It was failing to recognize a
 *          statement like  "Class<Byte> TYPE = (Class<Byte>)...;" because it was seeing
 *          'Class<Byte' in the cast expression as a less than expression, then failing 
 *          on the '>'.
 *      Changed createdName to use typeArguments instead of nonWildcardTypeArguments.
 *          Again, JLS doesn't seem to allow this, but java.lang.Class has an example of
 *          of this construct.
 *      Changed the 'this' alternative in primary to allow 'identifierSuffix' rather than
 *          just 'arguments'.  The case it couldn't handle was a call to an explicit
 *          generic method invocation (e.g. this.<E>doSomething()).  Using identifierSuffix
 *          may be overly aggressive--perhaps should create a more constrained thisSuffix rule?
 *      
 *  Version 1.0.4 -- Hiroaki Nakamura, May 3, 2007
 *
 *  Fixed formalParameterDecls, localVariableDeclaration, forInit,
 *  and forVarControl to use variableModifier* not 'final'? (annotation)?
 *
 *  Version 1.0.5 -- Terence, June 21, 2007
 *  --a[i].foo didn't work. Fixed unaryExpression
 *
 *  Version 1.0.6 -- John Ridgway, March 17, 2008
 *      Made "assert" a switchable keyword like "enum".
 *      Fixed compilationUnit to disallow "annotation importDeclaration ...".
 *      Changed "Identifier ('.' Identifier)*" to "qualifiedName" in more 
 *          places.
 *      Changed modifier* and/or variableModifier* to classOrInterfaceModifiers,
 *          modifiers or variableModifiers, as appropriate.
 *      Renamed "bound" to "typeBound" to better match language in the JLS.
 *      Added "memberDeclaration" which rewrites to methodDeclaration or 
 *      fieldDeclaration and pulled type into memberDeclaration.  So we parse 
 *          type and then move on to decide whether we're dealing with a field
 *          or a method.
 *      Modified "constructorDeclaration" to use "constructorBody" instead of
 *          "methodBody".  constructorBody starts with explicitConstructorInvocation,
 *          then goes on to blockStatement*.  Pulling explicitConstructorInvocation
 *          out of expressions allowed me to simplify "primary".
 *      Changed variableDeclarator to simplify it.
 *      Changed type to use classOrInterfaceType, thus simplifying it; of course
 *          I then had to add classOrInterfaceType, but it is used in several 
 *          places.
 *      Fixed annotations, old version allowed "@X(y,z)", which is illegal.
 *      Added optional comma to end of "elementValueArrayInitializer"; as per JLS.
 *      Changed annotationTypeElementRest to use normalClassDeclaration and 
 *          normalInterfaceDeclaration rather than classDeclaration and 
 *          interfaceDeclaration, thus getting rid of a couple of grammar ambiguities.
 *      Split localVariableDeclaration into localVariableDeclarationStatement
 *          (includes the terminating semi-colon) and localVariableDeclaration.  
 *          This allowed me to use localVariableDeclaration in "forInit" clauses,
 *           simplifying them.
 *      Changed switchBlockStatementGroup to use multiple labels.  This adds an
 *          ambiguity, but if one uses appropriately greedy parsing it yields the
 *           parse that is closest to the meaning of the switch statement.
 *      Renamed "forVarControl" to "enhancedForControl" -- JLS language.
 *      Added semantic predicates to test for shift operations rather than other
 *          things.  Thus, for instance, the string "< <" will never be treated
 *          as a left-shift operator.
 *      In "creator" we rule out "nonWildcardTypeArguments" on arrayCreation, 
 *          which are illegal.
 *      Moved "nonWildcardTypeArguments into innerCreator.
 *      Removed 'super' superSuffix from explicitGenericInvocation, since that
 *          is only used in explicitConstructorInvocation at the beginning of a
 *           constructorBody.  (This is part of the simplification of expressions
 *           mentioned earlier.)
 *      Simplified primary (got rid of those things that are only used in
 *          explicitConstructorInvocation).
 *      Lexer -- removed "Exponent?" from FloatingPointLiteral choice 4, since it
 *          led to an ambiguity.
 *
 *      This grammar successfully parses every .java file in the JDK 1.5 source 
 *          tree (excluding those whose file names include '-', which are not
 *          valid Java compilation units).
 *
 *  Known remaining problems:
 *      "Letter" and "JavaIDDigit" are wrong.  The actual specification of
 *      "Letter" should be "a character for which the method
 *      Character.isJavaIdentifierStart(int) returns true."  A "Java 
 *      letter-or-digit is a character for which the method 
 *      Character.isJavaIdentifierPart(int) returns true."
 */
public class JavaParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "Identifier", "ENUM", "FloatingPointLiteral", "CharacterLiteral", "StringLiteral", "HexLiteral", "OctalLiteral", "DecimalLiteral", "ASSERT", "HexDigit", "IntegerTypeSuffix", "Exponent", "FloatTypeSuffix", "EscapeSequence", "UnicodeEscape", "OctalEscape", "Letter", "JavaIDDigit", "WS", "COMMENT", "LINE_COMMENT", "'package'", "';'", "'import'", "'static'", "'.'", "'*'", "'public'", "'protected'", "'private'", "'abstract'", "'final'", "'strictfp'", "'class'", "'extends'", "'implements'", "'<'", "','", "'>'", "'&'", "'{'", "'}'", "'interface'", "'void'", "'['", "']'", "'throws'", "'='", "'native'", "'synchronized'", "'transient'", "'volatile'", "'boolean'", "'char'", "'byte'", "'short'", "'int'", "'long'", "'float'", "'double'", "'?'", "'super'", "'('", "')'", "'...'", "'this'", "'null'", "'true'", "'false'", "'@'", "'default'", "':'", "'if'", "'else'", "'for'", "'while'", "'do'", "'try'", "'finally'", "'switch'", "'return'", "'throw'", "'break'", "'continue'", "'catch'", "'case'", "'+='", "'-='", "'*='", "'/='", "'&='", "'|='", "'^='", "'%='", "'||'", "'&&'", "'|'", "'^'", "'=='", "'!='", "'instanceof'", "'+'", "'-'", "'/'", "'%'", "'++'", "'--'", "'~'", "'!'", "'new'"
    };
    public static final int HexLiteral=9;
    public static final int LINE_COMMENT=24;
    public static final int FloatTypeSuffix=16;
    public static final int OctalLiteral=10;
    public static final int IntegerTypeSuffix=14;
    public static final int CharacterLiteral=7;
    public static final int Exponent=15;
    public static final int EOF=-1;
    public static final int DecimalLiteral=11;
    public static final int ASSERT=12;
    public static final int HexDigit=13;
    public static final int Identifier=4;
    public static final int StringLiteral=8;
    public static final int WS=22;
    public static final int ENUM=5;
    public static final int UnicodeEscape=18;
    public static final int FloatingPointLiteral=6;
    public static final int JavaIDDigit=21;
    public static final int COMMENT=23;
    public static final int Letter=20;
    public static final int EscapeSequence=17;
    public static final int OctalEscape=19;

        public JavaParser(TokenStream input) {
            super(input);
            ruleMemo = new HashMap[407+1];
         }
        

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "/home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g"; }


    	/** Tracks how deep down into classes we're in (inner classes etc.) */
    	private int classDepth = 0;
    	
    	/** Whether we've already seen our super. */
    	private boolean foundSuper;
    	
    	private int lastTypeLine;
    	private int lastTypePos;
    	private String type;
    	private String superName;
    	private int superLine;
    	private int superPos;
    	private Map<String, Integer> methods = new HashMap<String, Integer>();
    	private Set<Integer> superCtorInvocations = new HashSet<Integer>();
    	private Set<Integer> superMethodInvocations = new HashSet<Integer>();
    	private Set<Integer> overrideAnnotationsLines = new HashSet<Integer>();
    	private String packageName;
    	private String currentAnnotations = "";
    	private Map<String, List<String>> annotations = 
    			new HashMap<String, List<String>>();
    		
    	public String getType() { return type; }
    	public Set<String> getMethods() { return new HashSet<String>(methods.keySet()); }
    	public Map<String, Integer> getMethodsWithLines() { return methods; }
    	private void addMethod(String method, int line) { 
    		methods.put(method, line);
    		if ("".equals(currentAnnotations)) {
    			annotations.put(method, Collections.<String>emptyList());
    		} else {
    			annotations.put(method, Arrays.asList(currentAnnotations.split("\\s+")));
    		}
    		currentAnnotations = "";
    	}
    	
    	public List<String> getAnnotations(String method) {
    		return annotations.get(method);
    	}
    	
    	private void addSuperConstructorInvocation(int line) {
    		if (classDepth == 1) 
    			superCtorInvocations.add(line); 
    	}
    	private void addSuperMethodInvocation(int line) {
    		if (classDepth == 1)
    			superMethodInvocations.add(line); 
    	}
    	private void setType(String type) { this.type = type; }
    	private void setSuper(String superName, int superLine, int superPos) {
    		this.superName = superName;
    		this.superLine = superLine;
    		this.superPos = superPos;
    	}
    	public String getSuperName() { return superName; }
    	public int getSuperLine() { return superLine; }
    	public int getSuperPos() { return superPos; }
    	public Set<Integer> getSuperConstructorInvocations() { 
    		return superCtorInvocations;
    	}
    	
    	public Set<Integer> getSuperMethodInvocations() {
    		return superMethodInvocations;
    	}
    	
    	public Set<Integer> getOverrideAnnotationsLines() {
    		return overrideAnnotationsLines;
    	}
    	
    	private void setPackage(String packageName) {
    		this.packageName = packageName;
    	}
    	
    	public String getFullName() {
    		if (packageName == null) {
    			return type;
    		} else {
    			return packageName + "." + type;
    		}
    	}



    // $ANTLR start compilationUnit
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:274:1: compilationUnit : ( ( '@' )=> annotations ( packageDeclaration ( importDeclaration )* ( typeDeclaration )* | classOrInterfaceDeclaration ( typeDeclaration )* ) | ( packageDeclaration )? ( importDeclaration )* ( typeDeclaration )* );
    public final void compilationUnit() throws RecognitionException {
        int compilationUnit_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 1) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:275:5: ( ( '@' )=> annotations ( packageDeclaration ( importDeclaration )* ( typeDeclaration )* | classOrInterfaceDeclaration ( typeDeclaration )* ) | ( packageDeclaration )? ( importDeclaration )* ( typeDeclaration )* )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==73) ) {
                int LA8_1 = input.LA(2);

                if ( (LA8_1==46) ) {
                    alt8=2;
                }
                else if ( (LA8_1==Identifier) ) {
                    int LA8_17 = input.LA(3);

                    if ( (synpred1()) ) {
                        alt8=1;
                    }
                    else if ( (true) ) {
                        alt8=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("274:1: compilationUnit : ( ( '@' )=> annotations ( packageDeclaration ( importDeclaration )* ( typeDeclaration )* | classOrInterfaceDeclaration ( typeDeclaration )* ) | ( packageDeclaration )? ( importDeclaration )* ( typeDeclaration )* );", 8, 17, input);

                        throw nvae;
                    }
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("274:1: compilationUnit : ( ( '@' )=> annotations ( packageDeclaration ( importDeclaration )* ( typeDeclaration )* | classOrInterfaceDeclaration ( typeDeclaration )* ) | ( packageDeclaration )? ( importDeclaration )* ( typeDeclaration )* );", 8, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA8_0==EOF||LA8_0==ENUM||(LA8_0>=25 && LA8_0<=28)||(LA8_0>=31 && LA8_0<=37)||LA8_0==46) ) {
                alt8=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("274:1: compilationUnit : ( ( '@' )=> annotations ( packageDeclaration ( importDeclaration )* ( typeDeclaration )* | classOrInterfaceDeclaration ( typeDeclaration )* ) | ( packageDeclaration )? ( importDeclaration )* ( typeDeclaration )* );", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:275:9: ( '@' )=> annotations ( packageDeclaration ( importDeclaration )* ( typeDeclaration )* | classOrInterfaceDeclaration ( typeDeclaration )* )
                    {
                    pushFollow(FOLLOW_annotations_in_compilationUnit72);
                    annotations();
                    _fsp--;
                    if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:276:9: ( packageDeclaration ( importDeclaration )* ( typeDeclaration )* | classOrInterfaceDeclaration ( typeDeclaration )* )
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0==25) ) {
                        alt4=1;
                    }
                    else if ( (LA4_0==ENUM||LA4_0==28||(LA4_0>=31 && LA4_0<=37)||LA4_0==46||LA4_0==73) ) {
                        alt4=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("276:9: ( packageDeclaration ( importDeclaration )* ( typeDeclaration )* | classOrInterfaceDeclaration ( typeDeclaration )* )", 4, 0, input);

                        throw nvae;
                    }
                    switch (alt4) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:276:13: packageDeclaration ( importDeclaration )* ( typeDeclaration )*
                            {
                            pushFollow(FOLLOW_packageDeclaration_in_compilationUnit86);
                            packageDeclaration();
                            _fsp--;
                            if (failed) return ;
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:276:32: ( importDeclaration )*
                            loop1:
                            do {
                                int alt1=2;
                                int LA1_0 = input.LA(1);

                                if ( (LA1_0==27) ) {
                                    alt1=1;
                                }


                                switch (alt1) {
                            	case 1 :
                            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: importDeclaration
                            	    {
                            	    pushFollow(FOLLOW_importDeclaration_in_compilationUnit88);
                            	    importDeclaration();
                            	    _fsp--;
                            	    if (failed) return ;

                            	    }
                            	    break;

                            	default :
                            	    break loop1;
                                }
                            } while (true);

                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:276:51: ( typeDeclaration )*
                            loop2:
                            do {
                                int alt2=2;
                                int LA2_0 = input.LA(1);

                                if ( (LA2_0==ENUM||LA2_0==26||LA2_0==28||(LA2_0>=31 && LA2_0<=37)||LA2_0==46||LA2_0==73) ) {
                                    alt2=1;
                                }


                                switch (alt2) {
                            	case 1 :
                            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: typeDeclaration
                            	    {
                            	    pushFollow(FOLLOW_typeDeclaration_in_compilationUnit91);
                            	    typeDeclaration();
                            	    _fsp--;
                            	    if (failed) return ;

                            	    }
                            	    break;

                            	default :
                            	    break loop2;
                                }
                            } while (true);


                            }
                            break;
                        case 2 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:277:13: classOrInterfaceDeclaration ( typeDeclaration )*
                            {
                            pushFollow(FOLLOW_classOrInterfaceDeclaration_in_compilationUnit106);
                            classOrInterfaceDeclaration();
                            _fsp--;
                            if (failed) return ;
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:277:41: ( typeDeclaration )*
                            loop3:
                            do {
                                int alt3=2;
                                int LA3_0 = input.LA(1);

                                if ( (LA3_0==ENUM||LA3_0==26||LA3_0==28||(LA3_0>=31 && LA3_0<=37)||LA3_0==46||LA3_0==73) ) {
                                    alt3=1;
                                }


                                switch (alt3) {
                            	case 1 :
                            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: typeDeclaration
                            	    {
                            	    pushFollow(FOLLOW_typeDeclaration_in_compilationUnit108);
                            	    typeDeclaration();
                            	    _fsp--;
                            	    if (failed) return ;

                            	    }
                            	    break;

                            	default :
                            	    break loop3;
                                }
                            } while (true);


                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:279:9: ( packageDeclaration )? ( importDeclaration )* ( typeDeclaration )*
                    {
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:279:9: ( packageDeclaration )?
                    int alt5=2;
                    int LA5_0 = input.LA(1);

                    if ( (LA5_0==25) ) {
                        alt5=1;
                    }
                    switch (alt5) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:279:10: packageDeclaration
                            {
                            pushFollow(FOLLOW_packageDeclaration_in_compilationUnit129);
                            packageDeclaration();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }

                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:279:31: ( importDeclaration )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==27) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: importDeclaration
                    	    {
                    	    pushFollow(FOLLOW_importDeclaration_in_compilationUnit133);
                    	    importDeclaration();
                    	    _fsp--;
                    	    if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);

                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:279:50: ( typeDeclaration )*
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( (LA7_0==ENUM||LA7_0==26||LA7_0==28||(LA7_0>=31 && LA7_0<=37)||LA7_0==46||LA7_0==73) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: typeDeclaration
                    	    {
                    	    pushFollow(FOLLOW_typeDeclaration_in_compilationUnit136);
                    	    typeDeclaration();
                    	    _fsp--;
                    	    if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop7;
                        }
                    } while (true);


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 1, compilationUnit_StartIndex); }
        }
        return ;
    }
    // $ANTLR end compilationUnit


    // $ANTLR start packageDeclaration
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:282:1: packageDeclaration : 'package' qualifiedName ';' ;
    public final void packageDeclaration() throws RecognitionException {
        int packageDeclaration_StartIndex = input.index();
        qualifiedName_return qualifiedName1 = null;


        try {
            if ( backtracking>0 && alreadyParsedRule(input, 2) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:283:5: ( 'package' qualifiedName ';' )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:283:9: 'package' qualifiedName ';'
            {
            match(input,25,FOLLOW_25_in_packageDeclaration156); if (failed) return ;
            pushFollow(FOLLOW_qualifiedName_in_packageDeclaration158);
            qualifiedName1=qualifiedName();
            _fsp--;
            if (failed) return ;
            match(input,26,FOLLOW_26_in_packageDeclaration160); if (failed) return ;
            if ( backtracking==0 ) {
               setPackage(input.toString(qualifiedName1.start,qualifiedName1.stop)); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 2, packageDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end packageDeclaration


    // $ANTLR start importDeclaration
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:286:1: importDeclaration : 'import' ( 'static' )? qualifiedName ( '.' '*' )? ';' ;
    public final void importDeclaration() throws RecognitionException {
        int importDeclaration_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 3) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:287:5: ( 'import' ( 'static' )? qualifiedName ( '.' '*' )? ';' )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:287:9: 'import' ( 'static' )? qualifiedName ( '.' '*' )? ';'
            {
            match(input,27,FOLLOW_27_in_importDeclaration185); if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:287:18: ( 'static' )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==28) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: 'static'
                    {
                    match(input,28,FOLLOW_28_in_importDeclaration187); if (failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_qualifiedName_in_importDeclaration190);
            qualifiedName();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:287:42: ( '.' '*' )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==29) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:287:43: '.' '*'
                    {
                    match(input,29,FOLLOW_29_in_importDeclaration193); if (failed) return ;
                    match(input,30,FOLLOW_30_in_importDeclaration195); if (failed) return ;

                    }
                    break;

            }

            match(input,26,FOLLOW_26_in_importDeclaration199); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 3, importDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end importDeclaration


    // $ANTLR start typeDeclaration
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:290:1: typeDeclaration : ( classOrInterfaceDeclaration | ';' );
    public final void typeDeclaration() throws RecognitionException {
        int typeDeclaration_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 4) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:291:5: ( classOrInterfaceDeclaration | ';' )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==ENUM||LA11_0==28||(LA11_0>=31 && LA11_0<=37)||LA11_0==46||LA11_0==73) ) {
                alt11=1;
            }
            else if ( (LA11_0==26) ) {
                alt11=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("290:1: typeDeclaration : ( classOrInterfaceDeclaration | ';' );", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:291:9: classOrInterfaceDeclaration
                    {
                    pushFollow(FOLLOW_classOrInterfaceDeclaration_in_typeDeclaration222);
                    classOrInterfaceDeclaration();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:292:9: ';'
                    {
                    match(input,26,FOLLOW_26_in_typeDeclaration232); if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 4, typeDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end typeDeclaration


    // $ANTLR start classOrInterfaceDeclaration
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:295:1: classOrInterfaceDeclaration : classOrInterfaceModifiers ( classDeclaration | interfaceDeclaration ) ;
    public final void classOrInterfaceDeclaration() throws RecognitionException {
        int classOrInterfaceDeclaration_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 5) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:296:5: ( classOrInterfaceModifiers ( classDeclaration | interfaceDeclaration ) )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:296:9: classOrInterfaceModifiers ( classDeclaration | interfaceDeclaration )
            {
            pushFollow(FOLLOW_classOrInterfaceModifiers_in_classOrInterfaceDeclaration255);
            classOrInterfaceModifiers();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:296:35: ( classDeclaration | interfaceDeclaration )
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==ENUM||LA12_0==37) ) {
                alt12=1;
            }
            else if ( (LA12_0==46||LA12_0==73) ) {
                alt12=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("296:35: ( classDeclaration | interfaceDeclaration )", 12, 0, input);

                throw nvae;
            }
            switch (alt12) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:296:36: classDeclaration
                    {
                    pushFollow(FOLLOW_classDeclaration_in_classOrInterfaceDeclaration258);
                    classDeclaration();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:296:55: interfaceDeclaration
                    {
                    pushFollow(FOLLOW_interfaceDeclaration_in_classOrInterfaceDeclaration262);
                    interfaceDeclaration();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 5, classOrInterfaceDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end classOrInterfaceDeclaration


    // $ANTLR start classOrInterfaceModifiers
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:299:1: classOrInterfaceModifiers : ( classOrInterfaceModifier )* ;
    public final void classOrInterfaceModifiers() throws RecognitionException {
        int classOrInterfaceModifiers_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 6) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:300:5: ( ( classOrInterfaceModifier )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:300:9: ( classOrInterfaceModifier )*
            {
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:300:9: ( classOrInterfaceModifier )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==73) ) {
                    int LA13_4 = input.LA(2);

                    if ( (LA13_4==Identifier) ) {
                        alt13=1;
                    }


                }
                else if ( (LA13_0==28||(LA13_0>=31 && LA13_0<=36)) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: classOrInterfaceModifier
            	    {
            	    pushFollow(FOLLOW_classOrInterfaceModifier_in_classOrInterfaceModifiers286);
            	    classOrInterfaceModifier();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 6, classOrInterfaceModifiers_StartIndex); }
        }
        return ;
    }
    // $ANTLR end classOrInterfaceModifiers


    // $ANTLR start classOrInterfaceModifier
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:303:1: classOrInterfaceModifier : ( annotation | 'public' | 'protected' | 'private' | 'abstract' | 'static' | 'final' | 'strictfp' );
    public final void classOrInterfaceModifier() throws RecognitionException {
        int classOrInterfaceModifier_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 7) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:304:5: ( annotation | 'public' | 'protected' | 'private' | 'abstract' | 'static' | 'final' | 'strictfp' )
            int alt14=8;
            switch ( input.LA(1) ) {
            case 73:
                {
                alt14=1;
                }
                break;
            case 31:
                {
                alt14=2;
                }
                break;
            case 32:
                {
                alt14=3;
                }
                break;
            case 33:
                {
                alt14=4;
                }
                break;
            case 34:
                {
                alt14=5;
                }
                break;
            case 28:
                {
                alt14=6;
                }
                break;
            case 35:
                {
                alt14=7;
                }
                break;
            case 36:
                {
                alt14=8;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("303:1: classOrInterfaceModifier : ( annotation | 'public' | 'protected' | 'private' | 'abstract' | 'static' | 'final' | 'strictfp' );", 14, 0, input);

                throw nvae;
            }

            switch (alt14) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:304:9: annotation
                    {
                    pushFollow(FOLLOW_annotation_in_classOrInterfaceModifier306);
                    annotation();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:305:9: 'public'
                    {
                    match(input,31,FOLLOW_31_in_classOrInterfaceModifier319); if (failed) return ;

                    }
                    break;
                case 3 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:306:9: 'protected'
                    {
                    match(input,32,FOLLOW_32_in_classOrInterfaceModifier334); if (failed) return ;

                    }
                    break;
                case 4 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:307:9: 'private'
                    {
                    match(input,33,FOLLOW_33_in_classOrInterfaceModifier346); if (failed) return ;

                    }
                    break;
                case 5 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:308:9: 'abstract'
                    {
                    match(input,34,FOLLOW_34_in_classOrInterfaceModifier360); if (failed) return ;

                    }
                    break;
                case 6 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:309:9: 'static'
                    {
                    match(input,28,FOLLOW_28_in_classOrInterfaceModifier373); if (failed) return ;

                    }
                    break;
                case 7 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:310:9: 'final'
                    {
                    match(input,35,FOLLOW_35_in_classOrInterfaceModifier388); if (failed) return ;

                    }
                    break;
                case 8 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:311:9: 'strictfp'
                    {
                    match(input,36,FOLLOW_36_in_classOrInterfaceModifier404); if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 7, classOrInterfaceModifier_StartIndex); }
        }
        return ;
    }
    // $ANTLR end classOrInterfaceModifier


    // $ANTLR start modifiers
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:314:1: modifiers : ( modifier )* ;
    public final void modifiers() throws RecognitionException {
        int modifiers_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 8) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:315:5: ( ( modifier )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:315:9: ( modifier )*
            {
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:315:9: ( modifier )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==73) ) {
                    int LA15_6 = input.LA(2);

                    if ( (LA15_6==Identifier) ) {
                        alt15=1;
                    }


                }
                else if ( (LA15_0==28||(LA15_0>=31 && LA15_0<=36)||(LA15_0>=52 && LA15_0<=55)) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: modifier
            	    {
            	    pushFollow(FOLLOW_modifier_in_modifiers426);
            	    modifier();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 8, modifiers_StartIndex); }
        }
        return ;
    }
    // $ANTLR end modifiers


    // $ANTLR start classDeclaration
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:318:1: classDeclaration : ( normalClassDeclaration | enumDeclaration );
    public final void classDeclaration() throws RecognitionException {
        int classDeclaration_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 9) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:319:5: ( normalClassDeclaration | enumDeclaration )
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==37) ) {
                alt16=1;
            }
            else if ( (LA16_0==ENUM) ) {
                alt16=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("318:1: classDeclaration : ( normalClassDeclaration | enumDeclaration );", 16, 0, input);

                throw nvae;
            }
            switch (alt16) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:319:9: normalClassDeclaration
                    {
                    if ( backtracking==0 ) {
                       classDepth++; 
                    }
                    pushFollow(FOLLOW_normalClassDeclaration_in_classDeclaration449);
                    normalClassDeclaration();
                    _fsp--;
                    if (failed) return ;
                    if ( backtracking==0 ) {
                       classDepth--; 
                    }

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:320:9: enumDeclaration
                    {
                    if ( backtracking==0 ) {
                       classDepth++; 
                    }
                    pushFollow(FOLLOW_enumDeclaration_in_classDeclaration463);
                    enumDeclaration();
                    _fsp--;
                    if (failed) return ;
                    if ( backtracking==0 ) {
                       classDepth--; 
                    }

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 9, classDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end classDeclaration


    // $ANTLR start normalClassDeclaration
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:323:1: normalClassDeclaration : 'class' Identifier ( typeParameters )? ( 'extends' type )? ( 'implements' typeList )? classBody ;
    public final void normalClassDeclaration() throws RecognitionException {
        int normalClassDeclaration_StartIndex = input.index();
        Token Identifier2=null;
        type_return type3 = null;


        try {
            if ( backtracking>0 && alreadyParsedRule(input, 10) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:324:5: ( 'class' Identifier ( typeParameters )? ( 'extends' type )? ( 'implements' typeList )? classBody )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:324:9: 'class' Identifier ( typeParameters )? ( 'extends' type )? ( 'implements' typeList )? classBody
            {
            match(input,37,FOLLOW_37_in_normalClassDeclaration488); if (failed) return ;
            Identifier2=(Token)input.LT(1);
            match(input,Identifier,FOLLOW_Identifier_in_normalClassDeclaration490); if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:324:28: ( typeParameters )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==40) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:324:29: typeParameters
                    {
                    pushFollow(FOLLOW_typeParameters_in_normalClassDeclaration493);
                    typeParameters();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            if ( backtracking==0 ) {

                  			// We track the type we're in 
                  			setType(Identifier2.getText()); 
                  	  
            }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:328:9: ( 'extends' type )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==38) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:328:10: 'extends' type
                    {
                    match(input,38,FOLLOW_38_in_normalClassDeclaration508); if (failed) return ;
                    pushFollow(FOLLOW_type_in_normalClassDeclaration510);
                    type3=type();
                    _fsp--;
                    if (failed) return ;
                    if ( backtracking==0 ) {
                       
                              		if (!foundSuper) setSuper(input.toString(type3.start,type3.stop), lastTypeLine, lastTypePos);
                              
                    }

                    }
                    break;

            }

            if ( backtracking==0 ) {

                          // Whether there's an extends clause or not, look no further 
                          foundSuper = true; 
                      
            }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:334:9: ( 'implements' typeList )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==39) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:334:10: 'implements' typeList
                    {
                    match(input,39,FOLLOW_39_in_normalClassDeclaration527); if (failed) return ;
                    pushFollow(FOLLOW_typeList_in_normalClassDeclaration529);
                    typeList();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_classBody_in_normalClassDeclaration541);
            classBody();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 10, normalClassDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end normalClassDeclaration


    // $ANTLR start typeParameters
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:338:1: typeParameters : '<' typeParameter ( ',' typeParameter )* '>' ;
    public final void typeParameters() throws RecognitionException {
        int typeParameters_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 11) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:339:5: ( '<' typeParameter ( ',' typeParameter )* '>' )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:339:9: '<' typeParameter ( ',' typeParameter )* '>'
            {
            match(input,40,FOLLOW_40_in_typeParameters564); if (failed) return ;
            pushFollow(FOLLOW_typeParameter_in_typeParameters566);
            typeParameter();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:339:27: ( ',' typeParameter )*
            loop20:
            do {
                int alt20=2;
                int LA20_0 = input.LA(1);

                if ( (LA20_0==41) ) {
                    alt20=1;
                }


                switch (alt20) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:339:28: ',' typeParameter
            	    {
            	    match(input,41,FOLLOW_41_in_typeParameters569); if (failed) return ;
            	    pushFollow(FOLLOW_typeParameter_in_typeParameters571);
            	    typeParameter();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop20;
                }
            } while (true);

            match(input,42,FOLLOW_42_in_typeParameters575); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 11, typeParameters_StartIndex); }
        }
        return ;
    }
    // $ANTLR end typeParameters


    // $ANTLR start typeParameter
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:342:1: typeParameter : Identifier ( 'extends' typeBound )? ;
    public final void typeParameter() throws RecognitionException {
        int typeParameter_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 12) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:343:5: ( Identifier ( 'extends' typeBound )? )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:343:9: Identifier ( 'extends' typeBound )?
            {
            match(input,Identifier,FOLLOW_Identifier_in_typeParameter594); if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:343:20: ( 'extends' typeBound )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==38) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:343:21: 'extends' typeBound
                    {
                    match(input,38,FOLLOW_38_in_typeParameter597); if (failed) return ;
                    pushFollow(FOLLOW_typeBound_in_typeParameter599);
                    typeBound();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 12, typeParameter_StartIndex); }
        }
        return ;
    }
    // $ANTLR end typeParameter


    // $ANTLR start typeBound
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:346:1: typeBound : type ( '&' type )* ;
    public final void typeBound() throws RecognitionException {
        int typeBound_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 13) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:347:5: ( type ( '&' type )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:347:9: type ( '&' type )*
            {
            pushFollow(FOLLOW_type_in_typeBound628);
            type();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:347:14: ( '&' type )*
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( (LA22_0==43) ) {
                    alt22=1;
                }


                switch (alt22) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:347:15: '&' type
            	    {
            	    match(input,43,FOLLOW_43_in_typeBound631); if (failed) return ;
            	    pushFollow(FOLLOW_type_in_typeBound633);
            	    type();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 13, typeBound_StartIndex); }
        }
        return ;
    }
    // $ANTLR end typeBound


    // $ANTLR start enumDeclaration
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:350:1: enumDeclaration : ENUM Identifier ( 'implements' typeList )? enumBody ;
    public final void enumDeclaration() throws RecognitionException {
        int enumDeclaration_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 14) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:351:5: ( ENUM Identifier ( 'implements' typeList )? enumBody )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:351:9: ENUM Identifier ( 'implements' typeList )? enumBody
            {
            match(input,ENUM,FOLLOW_ENUM_in_enumDeclaration654); if (failed) return ;
            match(input,Identifier,FOLLOW_Identifier_in_enumDeclaration656); if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:351:25: ( 'implements' typeList )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==39) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:351:26: 'implements' typeList
                    {
                    match(input,39,FOLLOW_39_in_enumDeclaration659); if (failed) return ;
                    pushFollow(FOLLOW_typeList_in_enumDeclaration661);
                    typeList();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_enumBody_in_enumDeclaration665);
            enumBody();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 14, enumDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end enumDeclaration


    // $ANTLR start enumBody
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:354:1: enumBody : '{' ( enumConstants )? ( ',' )? ( enumBodyDeclarations )? '}' ;
    public final void enumBody() throws RecognitionException {
        int enumBody_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 15) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:355:5: ( '{' ( enumConstants )? ( ',' )? ( enumBodyDeclarations )? '}' )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:355:9: '{' ( enumConstants )? ( ',' )? ( enumBodyDeclarations )? '}'
            {
            match(input,44,FOLLOW_44_in_enumBody684); if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:355:13: ( enumConstants )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==Identifier||LA24_0==73) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: enumConstants
                    {
                    pushFollow(FOLLOW_enumConstants_in_enumBody686);
                    enumConstants();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:355:28: ( ',' )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==41) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: ','
                    {
                    match(input,41,FOLLOW_41_in_enumBody689); if (failed) return ;

                    }
                    break;

            }

            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:355:33: ( enumBodyDeclarations )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==26) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: enumBodyDeclarations
                    {
                    pushFollow(FOLLOW_enumBodyDeclarations_in_enumBody692);
                    enumBodyDeclarations();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            match(input,45,FOLLOW_45_in_enumBody695); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 15, enumBody_StartIndex); }
        }
        return ;
    }
    // $ANTLR end enumBody


    // $ANTLR start enumConstants
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:358:1: enumConstants : enumConstant ( ',' enumConstant )* ;
    public final void enumConstants() throws RecognitionException {
        int enumConstants_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 16) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:359:5: ( enumConstant ( ',' enumConstant )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:359:9: enumConstant ( ',' enumConstant )*
            {
            pushFollow(FOLLOW_enumConstant_in_enumConstants714);
            enumConstant();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:359:22: ( ',' enumConstant )*
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( (LA27_0==41) ) {
                    int LA27_1 = input.LA(2);

                    if ( (LA27_1==Identifier||LA27_1==73) ) {
                        alt27=1;
                    }


                }


                switch (alt27) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:359:23: ',' enumConstant
            	    {
            	    match(input,41,FOLLOW_41_in_enumConstants717); if (failed) return ;
            	    pushFollow(FOLLOW_enumConstant_in_enumConstants719);
            	    enumConstant();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop27;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 16, enumConstants_StartIndex); }
        }
        return ;
    }
    // $ANTLR end enumConstants


    // $ANTLR start enumConstant
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:362:1: enumConstant : ( annotations )? Identifier ( arguments )? ( classBody )? ;
    public final void enumConstant() throws RecognitionException {
        int enumConstant_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 17) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:363:5: ( ( annotations )? Identifier ( arguments )? ( classBody )? )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:363:9: ( annotations )? Identifier ( arguments )? ( classBody )?
            {
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:363:9: ( annotations )?
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==73) ) {
                alt28=1;
            }
            switch (alt28) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: annotations
                    {
                    pushFollow(FOLLOW_annotations_in_enumConstant744);
                    annotations();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            match(input,Identifier,FOLLOW_Identifier_in_enumConstant747); if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:363:33: ( arguments )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==66) ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:363:34: arguments
                    {
                    pushFollow(FOLLOW_arguments_in_enumConstant750);
                    arguments();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:363:46: ( classBody )?
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( (LA30_0==44) ) {
                alt30=1;
            }
            switch (alt30) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:363:47: classBody
                    {
                    pushFollow(FOLLOW_classBody_in_enumConstant755);
                    classBody();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 17, enumConstant_StartIndex); }
        }
        return ;
    }
    // $ANTLR end enumConstant


    // $ANTLR start enumBodyDeclarations
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:366:1: enumBodyDeclarations : ';' ( classBodyDeclaration )* ;
    public final void enumBodyDeclarations() throws RecognitionException {
        int enumBodyDeclarations_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 18) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:367:5: ( ';' ( classBodyDeclaration )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:367:9: ';' ( classBodyDeclaration )*
            {
            match(input,26,FOLLOW_26_in_enumBodyDeclarations780); if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:367:13: ( classBodyDeclaration )*
            loop31:
            do {
                int alt31=2;
                int LA31_0 = input.LA(1);

                if ( ((LA31_0>=Identifier && LA31_0<=ENUM)||LA31_0==26||LA31_0==28||(LA31_0>=31 && LA31_0<=37)||LA31_0==40||LA31_0==44||(LA31_0>=46 && LA31_0<=47)||(LA31_0>=52 && LA31_0<=63)||LA31_0==73) ) {
                    alt31=1;
                }


                switch (alt31) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:367:14: classBodyDeclaration
            	    {
            	    pushFollow(FOLLOW_classBodyDeclaration_in_enumBodyDeclarations783);
            	    classBodyDeclaration();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop31;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 18, enumBodyDeclarations_StartIndex); }
        }
        return ;
    }
    // $ANTLR end enumBodyDeclarations


    // $ANTLR start interfaceDeclaration
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:370:1: interfaceDeclaration : ( normalInterfaceDeclaration | annotationTypeDeclaration );
    public final void interfaceDeclaration() throws RecognitionException {
        int interfaceDeclaration_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 19) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:371:5: ( normalInterfaceDeclaration | annotationTypeDeclaration )
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==46) ) {
                alt32=1;
            }
            else if ( (LA32_0==73) ) {
                alt32=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("370:1: interfaceDeclaration : ( normalInterfaceDeclaration | annotationTypeDeclaration );", 32, 0, input);

                throw nvae;
            }
            switch (alt32) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:371:9: normalInterfaceDeclaration
                    {
                    pushFollow(FOLLOW_normalInterfaceDeclaration_in_interfaceDeclaration808);
                    normalInterfaceDeclaration();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:372:9: annotationTypeDeclaration
                    {
                    pushFollow(FOLLOW_annotationTypeDeclaration_in_interfaceDeclaration818);
                    annotationTypeDeclaration();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 19, interfaceDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end interfaceDeclaration


    // $ANTLR start normalInterfaceDeclaration
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:375:1: normalInterfaceDeclaration : 'interface' Identifier ( typeParameters )? ( 'extends' typeList )? interfaceBody ;
    public final void normalInterfaceDeclaration() throws RecognitionException {
        int normalInterfaceDeclaration_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 20) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:376:5: ( 'interface' Identifier ( typeParameters )? ( 'extends' typeList )? interfaceBody )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:376:9: 'interface' Identifier ( typeParameters )? ( 'extends' typeList )? interfaceBody
            {
            match(input,46,FOLLOW_46_in_normalInterfaceDeclaration841); if (failed) return ;
            match(input,Identifier,FOLLOW_Identifier_in_normalInterfaceDeclaration843); if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:376:32: ( typeParameters )?
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==40) ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: typeParameters
                    {
                    pushFollow(FOLLOW_typeParameters_in_normalInterfaceDeclaration845);
                    typeParameters();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:376:48: ( 'extends' typeList )?
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( (LA34_0==38) ) {
                alt34=1;
            }
            switch (alt34) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:376:49: 'extends' typeList
                    {
                    match(input,38,FOLLOW_38_in_normalInterfaceDeclaration849); if (failed) return ;
                    pushFollow(FOLLOW_typeList_in_normalInterfaceDeclaration851);
                    typeList();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_interfaceBody_in_normalInterfaceDeclaration855);
            interfaceBody();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 20, normalInterfaceDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end normalInterfaceDeclaration


    // $ANTLR start typeList
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:379:1: typeList : type ( ',' type )* ;
    public final void typeList() throws RecognitionException {
        int typeList_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 21) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:380:5: ( type ( ',' type )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:380:9: type ( ',' type )*
            {
            pushFollow(FOLLOW_type_in_typeList878);
            type();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:380:14: ( ',' type )*
            loop35:
            do {
                int alt35=2;
                int LA35_0 = input.LA(1);

                if ( (LA35_0==41) ) {
                    alt35=1;
                }


                switch (alt35) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:380:15: ',' type
            	    {
            	    match(input,41,FOLLOW_41_in_typeList881); if (failed) return ;
            	    pushFollow(FOLLOW_type_in_typeList883);
            	    type();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop35;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 21, typeList_StartIndex); }
        }
        return ;
    }
    // $ANTLR end typeList


    // $ANTLR start classBody
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:383:1: classBody : '{' ( classBodyDeclaration )* '}' ;
    public final void classBody() throws RecognitionException {
        int classBody_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 22) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:384:5: ( '{' ( classBodyDeclaration )* '}' )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:384:9: '{' ( classBodyDeclaration )* '}'
            {
            match(input,44,FOLLOW_44_in_classBody908); if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:384:13: ( classBodyDeclaration )*
            loop36:
            do {
                int alt36=2;
                int LA36_0 = input.LA(1);

                if ( ((LA36_0>=Identifier && LA36_0<=ENUM)||LA36_0==26||LA36_0==28||(LA36_0>=31 && LA36_0<=37)||LA36_0==40||LA36_0==44||(LA36_0>=46 && LA36_0<=47)||(LA36_0>=52 && LA36_0<=63)||LA36_0==73) ) {
                    alt36=1;
                }


                switch (alt36) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: classBodyDeclaration
            	    {
            	    pushFollow(FOLLOW_classBodyDeclaration_in_classBody910);
            	    classBodyDeclaration();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop36;
                }
            } while (true);

            match(input,45,FOLLOW_45_in_classBody913); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 22, classBody_StartIndex); }
        }
        return ;
    }
    // $ANTLR end classBody


    // $ANTLR start interfaceBody
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:387:1: interfaceBody : '{' ( interfaceBodyDeclaration )* '}' ;
    public final void interfaceBody() throws RecognitionException {
        int interfaceBody_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 23) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:388:5: ( '{' ( interfaceBodyDeclaration )* '}' )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:388:9: '{' ( interfaceBodyDeclaration )* '}'
            {
            match(input,44,FOLLOW_44_in_interfaceBody936); if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:388:13: ( interfaceBodyDeclaration )*
            loop37:
            do {
                int alt37=2;
                int LA37_0 = input.LA(1);

                if ( ((LA37_0>=Identifier && LA37_0<=ENUM)||LA37_0==26||LA37_0==28||(LA37_0>=31 && LA37_0<=37)||LA37_0==40||(LA37_0>=46 && LA37_0<=47)||(LA37_0>=52 && LA37_0<=63)||LA37_0==73) ) {
                    alt37=1;
                }


                switch (alt37) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: interfaceBodyDeclaration
            	    {
            	    pushFollow(FOLLOW_interfaceBodyDeclaration_in_interfaceBody938);
            	    interfaceBodyDeclaration();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop37;
                }
            } while (true);

            match(input,45,FOLLOW_45_in_interfaceBody941); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 23, interfaceBody_StartIndex); }
        }
        return ;
    }
    // $ANTLR end interfaceBody


    // $ANTLR start classBodyDeclaration
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:391:1: classBodyDeclaration : ( ';' | ( 'static' )? block | modifiers memberDecl );
    public final void classBodyDeclaration() throws RecognitionException {
        int classBodyDeclaration_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 24) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:392:5: ( ';' | ( 'static' )? block | modifiers memberDecl )
            int alt39=3;
            switch ( input.LA(1) ) {
            case 26:
                {
                alt39=1;
                }
                break;
            case 28:
                {
                int LA39_2 = input.LA(2);

                if ( ((LA39_2>=Identifier && LA39_2<=ENUM)||LA39_2==28||(LA39_2>=31 && LA39_2<=37)||LA39_2==40||(LA39_2>=46 && LA39_2<=47)||(LA39_2>=52 && LA39_2<=63)||LA39_2==73) ) {
                    alt39=3;
                }
                else if ( (LA39_2==44) ) {
                    alt39=2;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("391:1: classBodyDeclaration : ( ';' | ( 'static' )? block | modifiers memberDecl );", 39, 2, input);

                    throw nvae;
                }
                }
                break;
            case 44:
                {
                alt39=2;
                }
                break;
            case Identifier:
            case ENUM:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 40:
            case 46:
            case 47:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 73:
                {
                alt39=3;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("391:1: classBodyDeclaration : ( ';' | ( 'static' )? block | modifiers memberDecl );", 39, 0, input);

                throw nvae;
            }

            switch (alt39) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:392:9: ';'
                    {
                    match(input,26,FOLLOW_26_in_classBodyDeclaration960); if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:393:9: ( 'static' )? block
                    {
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:393:9: ( 'static' )?
                    int alt38=2;
                    int LA38_0 = input.LA(1);

                    if ( (LA38_0==28) ) {
                        alt38=1;
                    }
                    switch (alt38) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: 'static'
                            {
                            match(input,28,FOLLOW_28_in_classBodyDeclaration970); if (failed) return ;

                            }
                            break;

                    }

                    pushFollow(FOLLOW_block_in_classBodyDeclaration973);
                    block();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 3 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:394:9: modifiers memberDecl
                    {
                    pushFollow(FOLLOW_modifiers_in_classBodyDeclaration983);
                    modifiers();
                    _fsp--;
                    if (failed) return ;
                    pushFollow(FOLLOW_memberDecl_in_classBodyDeclaration985);
                    memberDecl();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 24, classBodyDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end classBodyDeclaration


    // $ANTLR start memberDecl
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:397:1: memberDecl : ( genericMethodOrConstructorDecl | memberDeclaration | 'void' Identifier voidMethodDeclaratorRest | Identifier constructorDeclaratorRest | interfaceDeclaration | classDeclaration );
    public final void memberDecl() throws RecognitionException {
        int memberDecl_StartIndex = input.index();
        Token Identifier4=null;

        try {
            if ( backtracking>0 && alreadyParsedRule(input, 25) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:398:5: ( genericMethodOrConstructorDecl | memberDeclaration | 'void' Identifier voidMethodDeclaratorRest | Identifier constructorDeclaratorRest | interfaceDeclaration | classDeclaration )
            int alt40=6;
            switch ( input.LA(1) ) {
            case 40:
                {
                alt40=1;
                }
                break;
            case Identifier:
                {
                int LA40_2 = input.LA(2);

                if ( (LA40_2==66) ) {
                    alt40=4;
                }
                else if ( (LA40_2==Identifier||LA40_2==29||LA40_2==40||LA40_2==48) ) {
                    alt40=2;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("397:1: memberDecl : ( genericMethodOrConstructorDecl | memberDeclaration | 'void' Identifier voidMethodDeclaratorRest | Identifier constructorDeclaratorRest | interfaceDeclaration | classDeclaration );", 40, 2, input);

                    throw nvae;
                }
                }
                break;
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
                {
                alt40=2;
                }
                break;
            case 47:
                {
                alt40=3;
                }
                break;
            case 46:
            case 73:
                {
                alt40=5;
                }
                break;
            case ENUM:
            case 37:
                {
                alt40=6;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("397:1: memberDecl : ( genericMethodOrConstructorDecl | memberDeclaration | 'void' Identifier voidMethodDeclaratorRest | Identifier constructorDeclaratorRest | interfaceDeclaration | classDeclaration );", 40, 0, input);

                throw nvae;
            }

            switch (alt40) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:398:9: genericMethodOrConstructorDecl
                    {
                    pushFollow(FOLLOW_genericMethodOrConstructorDecl_in_memberDecl1008);
                    genericMethodOrConstructorDecl();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:399:9: memberDeclaration
                    {
                    pushFollow(FOLLOW_memberDeclaration_in_memberDecl1018);
                    memberDeclaration();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 3 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:400:9: 'void' Identifier voidMethodDeclaratorRest
                    {
                    match(input,47,FOLLOW_47_in_memberDecl1028); if (failed) return ;
                    Identifier4=(Token)input.LT(1);
                    match(input,Identifier,FOLLOW_Identifier_in_memberDecl1030); if (failed) return ;
                    pushFollow(FOLLOW_voidMethodDeclaratorRest_in_memberDecl1032);
                    voidMethodDeclaratorRest();
                    _fsp--;
                    if (failed) return ;
                    if ( backtracking==0 ) {
                       if (classDepth == 1) {addMethod(Identifier4.getText(), Identifier4.getLine());} 
                    }

                    }
                    break;
                case 4 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:401:9: Identifier constructorDeclaratorRest
                    {
                    match(input,Identifier,FOLLOW_Identifier_in_memberDecl1046); if (failed) return ;
                    pushFollow(FOLLOW_constructorDeclaratorRest_in_memberDecl1048);
                    constructorDeclaratorRest();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 5 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:402:9: interfaceDeclaration
                    {
                    pushFollow(FOLLOW_interfaceDeclaration_in_memberDecl1058);
                    interfaceDeclaration();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 6 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:403:9: classDeclaration
                    {
                    pushFollow(FOLLOW_classDeclaration_in_memberDecl1068);
                    classDeclaration();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 25, memberDecl_StartIndex); }
        }
        return ;
    }
    // $ANTLR end memberDecl


    // $ANTLR start memberDeclaration
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:406:1: memberDeclaration : type ( methodDeclaration | fieldDeclaration ) ;
    public final void memberDeclaration() throws RecognitionException {
        int memberDeclaration_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 26) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:407:5: ( type ( methodDeclaration | fieldDeclaration ) )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:407:9: type ( methodDeclaration | fieldDeclaration )
            {
            pushFollow(FOLLOW_type_in_memberDeclaration1091);
            type();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:407:14: ( methodDeclaration | fieldDeclaration )
            int alt41=2;
            int LA41_0 = input.LA(1);

            if ( (LA41_0==Identifier) ) {
                int LA41_1 = input.LA(2);

                if ( (LA41_1==66) ) {
                    alt41=1;
                }
                else if ( (LA41_1==26||LA41_1==41||LA41_1==48||LA41_1==51) ) {
                    alt41=2;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("407:14: ( methodDeclaration | fieldDeclaration )", 41, 1, input);

                    throw nvae;
                }
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("407:14: ( methodDeclaration | fieldDeclaration )", 41, 0, input);

                throw nvae;
            }
            switch (alt41) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:407:15: methodDeclaration
                    {
                    pushFollow(FOLLOW_methodDeclaration_in_memberDeclaration1094);
                    methodDeclaration();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:407:35: fieldDeclaration
                    {
                    pushFollow(FOLLOW_fieldDeclaration_in_memberDeclaration1098);
                    fieldDeclaration();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 26, memberDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end memberDeclaration


    // $ANTLR start genericMethodOrConstructorDecl
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:410:1: genericMethodOrConstructorDecl : typeParameters genericMethodOrConstructorRest ;
    public final void genericMethodOrConstructorDecl() throws RecognitionException {
        int genericMethodOrConstructorDecl_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 27) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:411:5: ( typeParameters genericMethodOrConstructorRest )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:411:9: typeParameters genericMethodOrConstructorRest
            {
            pushFollow(FOLLOW_typeParameters_in_genericMethodOrConstructorDecl1118);
            typeParameters();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_genericMethodOrConstructorRest_in_genericMethodOrConstructorDecl1120);
            genericMethodOrConstructorRest();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 27, genericMethodOrConstructorDecl_StartIndex); }
        }
        return ;
    }
    // $ANTLR end genericMethodOrConstructorDecl


    // $ANTLR start genericMethodOrConstructorRest
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:414:1: genericMethodOrConstructorRest : ( ( type | 'void' ) Identifier methodDeclaratorRest | Identifier constructorDeclaratorRest );
    public final void genericMethodOrConstructorRest() throws RecognitionException {
        int genericMethodOrConstructorRest_StartIndex = input.index();
        Token Identifier5=null;

        try {
            if ( backtracking>0 && alreadyParsedRule(input, 28) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:415:5: ( ( type | 'void' ) Identifier methodDeclaratorRest | Identifier constructorDeclaratorRest )
            int alt43=2;
            int LA43_0 = input.LA(1);

            if ( (LA43_0==Identifier) ) {
                int LA43_1 = input.LA(2);

                if ( (LA43_1==Identifier||LA43_1==29||LA43_1==40||LA43_1==48) ) {
                    alt43=1;
                }
                else if ( (LA43_1==66) ) {
                    alt43=2;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("414:1: genericMethodOrConstructorRest : ( ( type | 'void' ) Identifier methodDeclaratorRest | Identifier constructorDeclaratorRest );", 43, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA43_0==47||(LA43_0>=56 && LA43_0<=63)) ) {
                alt43=1;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("414:1: genericMethodOrConstructorRest : ( ( type | 'void' ) Identifier methodDeclaratorRest | Identifier constructorDeclaratorRest );", 43, 0, input);

                throw nvae;
            }
            switch (alt43) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:415:9: ( type | 'void' ) Identifier methodDeclaratorRest
                    {
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:415:9: ( type | 'void' )
                    int alt42=2;
                    int LA42_0 = input.LA(1);

                    if ( (LA42_0==Identifier||(LA42_0>=56 && LA42_0<=63)) ) {
                        alt42=1;
                    }
                    else if ( (LA42_0==47) ) {
                        alt42=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("415:9: ( type | 'void' )", 42, 0, input);

                        throw nvae;
                    }
                    switch (alt42) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:415:10: type
                            {
                            pushFollow(FOLLOW_type_in_genericMethodOrConstructorRest1144);
                            type();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;
                        case 2 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:415:17: 'void'
                            {
                            match(input,47,FOLLOW_47_in_genericMethodOrConstructorRest1148); if (failed) return ;

                            }
                            break;

                    }

                    Identifier5=(Token)input.LT(1);
                    match(input,Identifier,FOLLOW_Identifier_in_genericMethodOrConstructorRest1151); if (failed) return ;
                    pushFollow(FOLLOW_methodDeclaratorRest_in_genericMethodOrConstructorRest1153);
                    methodDeclaratorRest();
                    _fsp--;
                    if (failed) return ;
                    if ( backtracking==0 ) {
                       addMethod(Identifier5.getText(), Identifier5.getLine()); 
                    }

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:416:9: Identifier constructorDeclaratorRest
                    {
                    match(input,Identifier,FOLLOW_Identifier_in_genericMethodOrConstructorRest1167); if (failed) return ;
                    pushFollow(FOLLOW_constructorDeclaratorRest_in_genericMethodOrConstructorRest1169);
                    constructorDeclaratorRest();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 28, genericMethodOrConstructorRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end genericMethodOrConstructorRest


    // $ANTLR start methodDeclaration
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:419:1: methodDeclaration : Identifier methodDeclaratorRest ;
    public final void methodDeclaration() throws RecognitionException {
        int methodDeclaration_StartIndex = input.index();
        Token Identifier6=null;

        try {
            if ( backtracking>0 && alreadyParsedRule(input, 29) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:420:5: ( Identifier methodDeclaratorRest )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:420:9: Identifier methodDeclaratorRest
            {
            Identifier6=(Token)input.LT(1);
            match(input,Identifier,FOLLOW_Identifier_in_methodDeclaration1188); if (failed) return ;
            pushFollow(FOLLOW_methodDeclaratorRest_in_methodDeclaration1190);
            methodDeclaratorRest();
            _fsp--;
            if (failed) return ;
            if ( backtracking==0 ) {
               addMethod(Identifier6.getText(), Identifier6.getLine()); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 29, methodDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end methodDeclaration


    // $ANTLR start fieldDeclaration
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:423:1: fieldDeclaration : variableDeclarators ';' ;
    public final void fieldDeclaration() throws RecognitionException {
        int fieldDeclaration_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 30) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:424:5: ( variableDeclarators ';' )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:424:9: variableDeclarators ';'
            {
            pushFollow(FOLLOW_variableDeclarators_in_fieldDeclaration1213);
            variableDeclarators();
            _fsp--;
            if (failed) return ;
            match(input,26,FOLLOW_26_in_fieldDeclaration1215); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 30, fieldDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end fieldDeclaration


    // $ANTLR start interfaceBodyDeclaration
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:427:1: interfaceBodyDeclaration : ( modifiers interfaceMemberDecl | ';' );
    public final void interfaceBodyDeclaration() throws RecognitionException {
        int interfaceBodyDeclaration_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 31) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:428:5: ( modifiers interfaceMemberDecl | ';' )
            int alt44=2;
            int LA44_0 = input.LA(1);

            if ( ((LA44_0>=Identifier && LA44_0<=ENUM)||LA44_0==28||(LA44_0>=31 && LA44_0<=37)||LA44_0==40||(LA44_0>=46 && LA44_0<=47)||(LA44_0>=52 && LA44_0<=63)||LA44_0==73) ) {
                alt44=1;
            }
            else if ( (LA44_0==26) ) {
                alt44=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("427:1: interfaceBodyDeclaration : ( modifiers interfaceMemberDecl | ';' );", 44, 0, input);

                throw nvae;
            }
            switch (alt44) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:428:9: modifiers interfaceMemberDecl
                    {
                    pushFollow(FOLLOW_modifiers_in_interfaceBodyDeclaration1242);
                    modifiers();
                    _fsp--;
                    if (failed) return ;
                    pushFollow(FOLLOW_interfaceMemberDecl_in_interfaceBodyDeclaration1244);
                    interfaceMemberDecl();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:429:9: ';'
                    {
                    match(input,26,FOLLOW_26_in_interfaceBodyDeclaration1254); if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 31, interfaceBodyDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end interfaceBodyDeclaration


    // $ANTLR start interfaceMemberDecl
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:432:1: interfaceMemberDecl : ( interfaceMethodOrFieldDecl | interfaceGenericMethodDecl | 'void' Identifier voidInterfaceMethodDeclaratorRest | interfaceDeclaration | classDeclaration );
    public final void interfaceMemberDecl() throws RecognitionException {
        int interfaceMemberDecl_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 32) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:433:5: ( interfaceMethodOrFieldDecl | interfaceGenericMethodDecl | 'void' Identifier voidInterfaceMethodDeclaratorRest | interfaceDeclaration | classDeclaration )
            int alt45=5;
            switch ( input.LA(1) ) {
            case Identifier:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
                {
                alt45=1;
                }
                break;
            case 40:
                {
                alt45=2;
                }
                break;
            case 47:
                {
                alt45=3;
                }
                break;
            case 46:
            case 73:
                {
                alt45=4;
                }
                break;
            case ENUM:
            case 37:
                {
                alt45=5;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("432:1: interfaceMemberDecl : ( interfaceMethodOrFieldDecl | interfaceGenericMethodDecl | 'void' Identifier voidInterfaceMethodDeclaratorRest | interfaceDeclaration | classDeclaration );", 45, 0, input);

                throw nvae;
            }

            switch (alt45) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:433:9: interfaceMethodOrFieldDecl
                    {
                    pushFollow(FOLLOW_interfaceMethodOrFieldDecl_in_interfaceMemberDecl1273);
                    interfaceMethodOrFieldDecl();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:434:9: interfaceGenericMethodDecl
                    {
                    pushFollow(FOLLOW_interfaceGenericMethodDecl_in_interfaceMemberDecl1283);
                    interfaceGenericMethodDecl();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 3 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:435:9: 'void' Identifier voidInterfaceMethodDeclaratorRest
                    {
                    match(input,47,FOLLOW_47_in_interfaceMemberDecl1293); if (failed) return ;
                    match(input,Identifier,FOLLOW_Identifier_in_interfaceMemberDecl1295); if (failed) return ;
                    pushFollow(FOLLOW_voidInterfaceMethodDeclaratorRest_in_interfaceMemberDecl1297);
                    voidInterfaceMethodDeclaratorRest();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 4 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:436:9: interfaceDeclaration
                    {
                    pushFollow(FOLLOW_interfaceDeclaration_in_interfaceMemberDecl1307);
                    interfaceDeclaration();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 5 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:437:9: classDeclaration
                    {
                    pushFollow(FOLLOW_classDeclaration_in_interfaceMemberDecl1317);
                    classDeclaration();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 32, interfaceMemberDecl_StartIndex); }
        }
        return ;
    }
    // $ANTLR end interfaceMemberDecl


    // $ANTLR start interfaceMethodOrFieldDecl
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:440:1: interfaceMethodOrFieldDecl : type Identifier interfaceMethodOrFieldRest ;
    public final void interfaceMethodOrFieldDecl() throws RecognitionException {
        int interfaceMethodOrFieldDecl_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 33) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:441:5: ( type Identifier interfaceMethodOrFieldRest )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:441:9: type Identifier interfaceMethodOrFieldRest
            {
            pushFollow(FOLLOW_type_in_interfaceMethodOrFieldDecl1340);
            type();
            _fsp--;
            if (failed) return ;
            match(input,Identifier,FOLLOW_Identifier_in_interfaceMethodOrFieldDecl1342); if (failed) return ;
            pushFollow(FOLLOW_interfaceMethodOrFieldRest_in_interfaceMethodOrFieldDecl1344);
            interfaceMethodOrFieldRest();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 33, interfaceMethodOrFieldDecl_StartIndex); }
        }
        return ;
    }
    // $ANTLR end interfaceMethodOrFieldDecl


    // $ANTLR start interfaceMethodOrFieldRest
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:444:1: interfaceMethodOrFieldRest : ( constantDeclaratorsRest ';' | interfaceMethodDeclaratorRest );
    public final void interfaceMethodOrFieldRest() throws RecognitionException {
        int interfaceMethodOrFieldRest_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 34) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:445:5: ( constantDeclaratorsRest ';' | interfaceMethodDeclaratorRest )
            int alt46=2;
            int LA46_0 = input.LA(1);

            if ( (LA46_0==48||LA46_0==51) ) {
                alt46=1;
            }
            else if ( (LA46_0==66) ) {
                alt46=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("444:1: interfaceMethodOrFieldRest : ( constantDeclaratorsRest ';' | interfaceMethodDeclaratorRest );", 46, 0, input);

                throw nvae;
            }
            switch (alt46) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:445:9: constantDeclaratorsRest ';'
                    {
                    pushFollow(FOLLOW_constantDeclaratorsRest_in_interfaceMethodOrFieldRest1367);
                    constantDeclaratorsRest();
                    _fsp--;
                    if (failed) return ;
                    match(input,26,FOLLOW_26_in_interfaceMethodOrFieldRest1369); if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:446:9: interfaceMethodDeclaratorRest
                    {
                    pushFollow(FOLLOW_interfaceMethodDeclaratorRest_in_interfaceMethodOrFieldRest1379);
                    interfaceMethodDeclaratorRest();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 34, interfaceMethodOrFieldRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end interfaceMethodOrFieldRest


    // $ANTLR start methodDeclaratorRest
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:449:1: methodDeclaratorRest : formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ( methodBody | ';' ) ;
    public final void methodDeclaratorRest() throws RecognitionException {
        int methodDeclaratorRest_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 35) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:450:5: ( formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ( methodBody | ';' ) )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:450:9: formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ( methodBody | ';' )
            {
            pushFollow(FOLLOW_formalParameters_in_methodDeclaratorRest1402);
            formalParameters();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:450:26: ( '[' ']' )*
            loop47:
            do {
                int alt47=2;
                int LA47_0 = input.LA(1);

                if ( (LA47_0==48) ) {
                    alt47=1;
                }


                switch (alt47) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:450:27: '[' ']'
            	    {
            	    match(input,48,FOLLOW_48_in_methodDeclaratorRest1405); if (failed) return ;
            	    match(input,49,FOLLOW_49_in_methodDeclaratorRest1407); if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop47;
                }
            } while (true);

            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:451:9: ( 'throws' qualifiedNameList )?
            int alt48=2;
            int LA48_0 = input.LA(1);

            if ( (LA48_0==50) ) {
                alt48=1;
            }
            switch (alt48) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:451:10: 'throws' qualifiedNameList
                    {
                    match(input,50,FOLLOW_50_in_methodDeclaratorRest1420); if (failed) return ;
                    pushFollow(FOLLOW_qualifiedNameList_in_methodDeclaratorRest1422);
                    qualifiedNameList();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:452:9: ( methodBody | ';' )
            int alt49=2;
            int LA49_0 = input.LA(1);

            if ( (LA49_0==44) ) {
                alt49=1;
            }
            else if ( (LA49_0==26) ) {
                alt49=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("452:9: ( methodBody | ';' )", 49, 0, input);

                throw nvae;
            }
            switch (alt49) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:452:13: methodBody
                    {
                    pushFollow(FOLLOW_methodBody_in_methodDeclaratorRest1438);
                    methodBody();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:453:13: ';'
                    {
                    match(input,26,FOLLOW_26_in_methodDeclaratorRest1452); if (failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 35, methodDeclaratorRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end methodDeclaratorRest


    // $ANTLR start voidMethodDeclaratorRest
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:457:1: voidMethodDeclaratorRest : formalParameters ( 'throws' qualifiedNameList )? ( methodBody | ';' ) ;
    public final void voidMethodDeclaratorRest() throws RecognitionException {
        int voidMethodDeclaratorRest_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 36) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:458:5: ( formalParameters ( 'throws' qualifiedNameList )? ( methodBody | ';' ) )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:458:9: formalParameters ( 'throws' qualifiedNameList )? ( methodBody | ';' )
            {
            pushFollow(FOLLOW_formalParameters_in_voidMethodDeclaratorRest1485);
            formalParameters();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:458:26: ( 'throws' qualifiedNameList )?
            int alt50=2;
            int LA50_0 = input.LA(1);

            if ( (LA50_0==50) ) {
                alt50=1;
            }
            switch (alt50) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:458:27: 'throws' qualifiedNameList
                    {
                    match(input,50,FOLLOW_50_in_voidMethodDeclaratorRest1488); if (failed) return ;
                    pushFollow(FOLLOW_qualifiedNameList_in_voidMethodDeclaratorRest1490);
                    qualifiedNameList();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:459:9: ( methodBody | ';' )
            int alt51=2;
            int LA51_0 = input.LA(1);

            if ( (LA51_0==44) ) {
                alt51=1;
            }
            else if ( (LA51_0==26) ) {
                alt51=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("459:9: ( methodBody | ';' )", 51, 0, input);

                throw nvae;
            }
            switch (alt51) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:459:13: methodBody
                    {
                    pushFollow(FOLLOW_methodBody_in_voidMethodDeclaratorRest1506);
                    methodBody();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:460:13: ';'
                    {
                    match(input,26,FOLLOW_26_in_voidMethodDeclaratorRest1520); if (failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 36, voidMethodDeclaratorRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end voidMethodDeclaratorRest


    // $ANTLR start interfaceMethodDeclaratorRest
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:464:1: interfaceMethodDeclaratorRest : formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ';' ;
    public final void interfaceMethodDeclaratorRest() throws RecognitionException {
        int interfaceMethodDeclaratorRest_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 37) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:465:5: ( formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ';' )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:465:9: formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ';'
            {
            pushFollow(FOLLOW_formalParameters_in_interfaceMethodDeclaratorRest1553);
            formalParameters();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:465:26: ( '[' ']' )*
            loop52:
            do {
                int alt52=2;
                int LA52_0 = input.LA(1);

                if ( (LA52_0==48) ) {
                    alt52=1;
                }


                switch (alt52) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:465:27: '[' ']'
            	    {
            	    match(input,48,FOLLOW_48_in_interfaceMethodDeclaratorRest1556); if (failed) return ;
            	    match(input,49,FOLLOW_49_in_interfaceMethodDeclaratorRest1558); if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop52;
                }
            } while (true);

            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:465:37: ( 'throws' qualifiedNameList )?
            int alt53=2;
            int LA53_0 = input.LA(1);

            if ( (LA53_0==50) ) {
                alt53=1;
            }
            switch (alt53) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:465:38: 'throws' qualifiedNameList
                    {
                    match(input,50,FOLLOW_50_in_interfaceMethodDeclaratorRest1563); if (failed) return ;
                    pushFollow(FOLLOW_qualifiedNameList_in_interfaceMethodDeclaratorRest1565);
                    qualifiedNameList();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            match(input,26,FOLLOW_26_in_interfaceMethodDeclaratorRest1569); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 37, interfaceMethodDeclaratorRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end interfaceMethodDeclaratorRest


    // $ANTLR start interfaceGenericMethodDecl
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:468:1: interfaceGenericMethodDecl : typeParameters ( type | 'void' ) Identifier interfaceMethodDeclaratorRest ;
    public final void interfaceGenericMethodDecl() throws RecognitionException {
        int interfaceGenericMethodDecl_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 38) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:469:5: ( typeParameters ( type | 'void' ) Identifier interfaceMethodDeclaratorRest )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:469:9: typeParameters ( type | 'void' ) Identifier interfaceMethodDeclaratorRest
            {
            pushFollow(FOLLOW_typeParameters_in_interfaceGenericMethodDecl1592);
            typeParameters();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:469:24: ( type | 'void' )
            int alt54=2;
            int LA54_0 = input.LA(1);

            if ( (LA54_0==Identifier||(LA54_0>=56 && LA54_0<=63)) ) {
                alt54=1;
            }
            else if ( (LA54_0==47) ) {
                alt54=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("469:24: ( type | 'void' )", 54, 0, input);

                throw nvae;
            }
            switch (alt54) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:469:25: type
                    {
                    pushFollow(FOLLOW_type_in_interfaceGenericMethodDecl1595);
                    type();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:469:32: 'void'
                    {
                    match(input,47,FOLLOW_47_in_interfaceGenericMethodDecl1599); if (failed) return ;

                    }
                    break;

            }

            match(input,Identifier,FOLLOW_Identifier_in_interfaceGenericMethodDecl1602); if (failed) return ;
            pushFollow(FOLLOW_interfaceMethodDeclaratorRest_in_interfaceGenericMethodDecl1612);
            interfaceMethodDeclaratorRest();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 38, interfaceGenericMethodDecl_StartIndex); }
        }
        return ;
    }
    // $ANTLR end interfaceGenericMethodDecl


    // $ANTLR start voidInterfaceMethodDeclaratorRest
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:473:1: voidInterfaceMethodDeclaratorRest : formalParameters ( 'throws' qualifiedNameList )? ';' ;
    public final void voidInterfaceMethodDeclaratorRest() throws RecognitionException {
        int voidInterfaceMethodDeclaratorRest_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 39) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:474:5: ( formalParameters ( 'throws' qualifiedNameList )? ';' )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:474:9: formalParameters ( 'throws' qualifiedNameList )? ';'
            {
            pushFollow(FOLLOW_formalParameters_in_voidInterfaceMethodDeclaratorRest1635);
            formalParameters();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:474:26: ( 'throws' qualifiedNameList )?
            int alt55=2;
            int LA55_0 = input.LA(1);

            if ( (LA55_0==50) ) {
                alt55=1;
            }
            switch (alt55) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:474:27: 'throws' qualifiedNameList
                    {
                    match(input,50,FOLLOW_50_in_voidInterfaceMethodDeclaratorRest1638); if (failed) return ;
                    pushFollow(FOLLOW_qualifiedNameList_in_voidInterfaceMethodDeclaratorRest1640);
                    qualifiedNameList();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            match(input,26,FOLLOW_26_in_voidInterfaceMethodDeclaratorRest1644); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 39, voidInterfaceMethodDeclaratorRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end voidInterfaceMethodDeclaratorRest


    // $ANTLR start constructorDeclaratorRest
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:477:1: constructorDeclaratorRest : formalParameters ( 'throws' qualifiedNameList )? constructorBody ;
    public final void constructorDeclaratorRest() throws RecognitionException {
        int constructorDeclaratorRest_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 40) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:478:5: ( formalParameters ( 'throws' qualifiedNameList )? constructorBody )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:478:9: formalParameters ( 'throws' qualifiedNameList )? constructorBody
            {
            pushFollow(FOLLOW_formalParameters_in_constructorDeclaratorRest1667);
            formalParameters();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:478:26: ( 'throws' qualifiedNameList )?
            int alt56=2;
            int LA56_0 = input.LA(1);

            if ( (LA56_0==50) ) {
                alt56=1;
            }
            switch (alt56) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:478:27: 'throws' qualifiedNameList
                    {
                    match(input,50,FOLLOW_50_in_constructorDeclaratorRest1670); if (failed) return ;
                    pushFollow(FOLLOW_qualifiedNameList_in_constructorDeclaratorRest1672);
                    qualifiedNameList();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_constructorBody_in_constructorDeclaratorRest1676);
            constructorBody();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 40, constructorDeclaratorRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end constructorDeclaratorRest


    // $ANTLR start constantDeclarator
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:481:1: constantDeclarator : Identifier constantDeclaratorRest ;
    public final void constantDeclarator() throws RecognitionException {
        int constantDeclarator_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 41) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:482:5: ( Identifier constantDeclaratorRest )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:482:9: Identifier constantDeclaratorRest
            {
            match(input,Identifier,FOLLOW_Identifier_in_constantDeclarator1695); if (failed) return ;
            pushFollow(FOLLOW_constantDeclaratorRest_in_constantDeclarator1697);
            constantDeclaratorRest();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 41, constantDeclarator_StartIndex); }
        }
        return ;
    }
    // $ANTLR end constantDeclarator


    // $ANTLR start variableDeclarators
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:485:1: variableDeclarators : variableDeclarator ( ',' variableDeclarator )* ;
    public final void variableDeclarators() throws RecognitionException {
        int variableDeclarators_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 42) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:486:5: ( variableDeclarator ( ',' variableDeclarator )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:486:9: variableDeclarator ( ',' variableDeclarator )*
            {
            pushFollow(FOLLOW_variableDeclarator_in_variableDeclarators1720);
            variableDeclarator();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:486:28: ( ',' variableDeclarator )*
            loop57:
            do {
                int alt57=2;
                int LA57_0 = input.LA(1);

                if ( (LA57_0==41) ) {
                    alt57=1;
                }


                switch (alt57) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:486:29: ',' variableDeclarator
            	    {
            	    match(input,41,FOLLOW_41_in_variableDeclarators1723); if (failed) return ;
            	    pushFollow(FOLLOW_variableDeclarator_in_variableDeclarators1725);
            	    variableDeclarator();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop57;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 42, variableDeclarators_StartIndex); }
        }
        return ;
    }
    // $ANTLR end variableDeclarators


    // $ANTLR start variableDeclarator
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:489:1: variableDeclarator : variableDeclaratorId ( '=' variableInitializer )? ;
    public final void variableDeclarator() throws RecognitionException {
        int variableDeclarator_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 43) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:490:5: ( variableDeclaratorId ( '=' variableInitializer )? )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:490:9: variableDeclaratorId ( '=' variableInitializer )?
            {
            pushFollow(FOLLOW_variableDeclaratorId_in_variableDeclarator1746);
            variableDeclaratorId();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:490:30: ( '=' variableInitializer )?
            int alt58=2;
            int LA58_0 = input.LA(1);

            if ( (LA58_0==51) ) {
                alt58=1;
            }
            switch (alt58) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:490:31: '=' variableInitializer
                    {
                    match(input,51,FOLLOW_51_in_variableDeclarator1749); if (failed) return ;
                    pushFollow(FOLLOW_variableInitializer_in_variableDeclarator1751);
                    variableInitializer();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 43, variableDeclarator_StartIndex); }
        }
        return ;
    }
    // $ANTLR end variableDeclarator


    // $ANTLR start constantDeclaratorsRest
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:493:1: constantDeclaratorsRest : constantDeclaratorRest ( ',' constantDeclarator )* ;
    public final void constantDeclaratorsRest() throws RecognitionException {
        int constantDeclaratorsRest_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 44) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:494:5: ( constantDeclaratorRest ( ',' constantDeclarator )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:494:9: constantDeclaratorRest ( ',' constantDeclarator )*
            {
            pushFollow(FOLLOW_constantDeclaratorRest_in_constantDeclaratorsRest1776);
            constantDeclaratorRest();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:494:32: ( ',' constantDeclarator )*
            loop59:
            do {
                int alt59=2;
                int LA59_0 = input.LA(1);

                if ( (LA59_0==41) ) {
                    alt59=1;
                }


                switch (alt59) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:494:33: ',' constantDeclarator
            	    {
            	    match(input,41,FOLLOW_41_in_constantDeclaratorsRest1779); if (failed) return ;
            	    pushFollow(FOLLOW_constantDeclarator_in_constantDeclaratorsRest1781);
            	    constantDeclarator();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop59;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 44, constantDeclaratorsRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end constantDeclaratorsRest


    // $ANTLR start constantDeclaratorRest
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:497:1: constantDeclaratorRest : ( '[' ']' )* '=' variableInitializer ;
    public final void constantDeclaratorRest() throws RecognitionException {
        int constantDeclaratorRest_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 45) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:498:5: ( ( '[' ']' )* '=' variableInitializer )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:498:9: ( '[' ']' )* '=' variableInitializer
            {
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:498:9: ( '[' ']' )*
            loop60:
            do {
                int alt60=2;
                int LA60_0 = input.LA(1);

                if ( (LA60_0==48) ) {
                    alt60=1;
                }


                switch (alt60) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:498:10: '[' ']'
            	    {
            	    match(input,48,FOLLOW_48_in_constantDeclaratorRest1803); if (failed) return ;
            	    match(input,49,FOLLOW_49_in_constantDeclaratorRest1805); if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop60;
                }
            } while (true);

            match(input,51,FOLLOW_51_in_constantDeclaratorRest1809); if (failed) return ;
            pushFollow(FOLLOW_variableInitializer_in_constantDeclaratorRest1811);
            variableInitializer();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 45, constantDeclaratorRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end constantDeclaratorRest


    // $ANTLR start variableDeclaratorId
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:501:1: variableDeclaratorId : Identifier ( '[' ']' )* ;
    public final void variableDeclaratorId() throws RecognitionException {
        int variableDeclaratorId_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 46) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:502:5: ( Identifier ( '[' ']' )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:502:9: Identifier ( '[' ']' )*
            {
            match(input,Identifier,FOLLOW_Identifier_in_variableDeclaratorId1834); if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:502:20: ( '[' ']' )*
            loop61:
            do {
                int alt61=2;
                int LA61_0 = input.LA(1);

                if ( (LA61_0==48) ) {
                    alt61=1;
                }


                switch (alt61) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:502:21: '[' ']'
            	    {
            	    match(input,48,FOLLOW_48_in_variableDeclaratorId1837); if (failed) return ;
            	    match(input,49,FOLLOW_49_in_variableDeclaratorId1839); if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop61;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 46, variableDeclaratorId_StartIndex); }
        }
        return ;
    }
    // $ANTLR end variableDeclaratorId


    // $ANTLR start variableInitializer
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:505:1: variableInitializer : ( arrayInitializer | expression );
    public final void variableInitializer() throws RecognitionException {
        int variableInitializer_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 47) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:506:5: ( arrayInitializer | expression )
            int alt62=2;
            int LA62_0 = input.LA(1);

            if ( (LA62_0==44) ) {
                alt62=1;
            }
            else if ( (LA62_0==Identifier||(LA62_0>=FloatingPointLiteral && LA62_0<=DecimalLiteral)||LA62_0==47||(LA62_0>=56 && LA62_0<=63)||(LA62_0>=65 && LA62_0<=66)||(LA62_0>=69 && LA62_0<=72)||(LA62_0>=105 && LA62_0<=106)||(LA62_0>=109 && LA62_0<=113)) ) {
                alt62=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("505:1: variableInitializer : ( arrayInitializer | expression );", 62, 0, input);

                throw nvae;
            }
            switch (alt62) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:506:9: arrayInitializer
                    {
                    pushFollow(FOLLOW_arrayInitializer_in_variableInitializer1860);
                    arrayInitializer();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:507:9: expression
                    {
                    pushFollow(FOLLOW_expression_in_variableInitializer1870);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 47, variableInitializer_StartIndex); }
        }
        return ;
    }
    // $ANTLR end variableInitializer


    // $ANTLR start arrayInitializer
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:510:1: arrayInitializer : '{' ( variableInitializer ( ',' variableInitializer )* ( ',' )? )? '}' ;
    public final void arrayInitializer() throws RecognitionException {
        int arrayInitializer_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 48) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:511:5: ( '{' ( variableInitializer ( ',' variableInitializer )* ( ',' )? )? '}' )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:511:9: '{' ( variableInitializer ( ',' variableInitializer )* ( ',' )? )? '}'
            {
            match(input,44,FOLLOW_44_in_arrayInitializer1897); if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:511:13: ( variableInitializer ( ',' variableInitializer )* ( ',' )? )?
            int alt65=2;
            int LA65_0 = input.LA(1);

            if ( (LA65_0==Identifier||(LA65_0>=FloatingPointLiteral && LA65_0<=DecimalLiteral)||LA65_0==44||LA65_0==47||(LA65_0>=56 && LA65_0<=63)||(LA65_0>=65 && LA65_0<=66)||(LA65_0>=69 && LA65_0<=72)||(LA65_0>=105 && LA65_0<=106)||(LA65_0>=109 && LA65_0<=113)) ) {
                alt65=1;
            }
            switch (alt65) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:511:14: variableInitializer ( ',' variableInitializer )* ( ',' )?
                    {
                    pushFollow(FOLLOW_variableInitializer_in_arrayInitializer1900);
                    variableInitializer();
                    _fsp--;
                    if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:511:34: ( ',' variableInitializer )*
                    loop63:
                    do {
                        int alt63=2;
                        int LA63_0 = input.LA(1);

                        if ( (LA63_0==41) ) {
                            int LA63_1 = input.LA(2);

                            if ( (LA63_1==Identifier||(LA63_1>=FloatingPointLiteral && LA63_1<=DecimalLiteral)||LA63_1==44||LA63_1==47||(LA63_1>=56 && LA63_1<=63)||(LA63_1>=65 && LA63_1<=66)||(LA63_1>=69 && LA63_1<=72)||(LA63_1>=105 && LA63_1<=106)||(LA63_1>=109 && LA63_1<=113)) ) {
                                alt63=1;
                            }


                        }


                        switch (alt63) {
                    	case 1 :
                    	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:511:35: ',' variableInitializer
                    	    {
                    	    match(input,41,FOLLOW_41_in_arrayInitializer1903); if (failed) return ;
                    	    pushFollow(FOLLOW_variableInitializer_in_arrayInitializer1905);
                    	    variableInitializer();
                    	    _fsp--;
                    	    if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop63;
                        }
                    } while (true);

                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:511:61: ( ',' )?
                    int alt64=2;
                    int LA64_0 = input.LA(1);

                    if ( (LA64_0==41) ) {
                        alt64=1;
                    }
                    switch (alt64) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:511:62: ','
                            {
                            match(input,41,FOLLOW_41_in_arrayInitializer1910); if (failed) return ;

                            }
                            break;

                    }


                    }
                    break;

            }

            match(input,45,FOLLOW_45_in_arrayInitializer1917); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 48, arrayInitializer_StartIndex); }
        }
        return ;
    }
    // $ANTLR end arrayInitializer


    // $ANTLR start modifier
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:514:1: modifier : ( annotation | 'public' | 'protected' | 'private' | 'static' | 'abstract' | 'final' | 'native' | 'synchronized' | 'transient' | 'volatile' | 'strictfp' );
    public final void modifier() throws RecognitionException {
        int modifier_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 49) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:515:5: ( annotation | 'public' | 'protected' | 'private' | 'static' | 'abstract' | 'final' | 'native' | 'synchronized' | 'transient' | 'volatile' | 'strictfp' )
            int alt66=12;
            switch ( input.LA(1) ) {
            case 73:
                {
                alt66=1;
                }
                break;
            case 31:
                {
                alt66=2;
                }
                break;
            case 32:
                {
                alt66=3;
                }
                break;
            case 33:
                {
                alt66=4;
                }
                break;
            case 28:
                {
                alt66=5;
                }
                break;
            case 34:
                {
                alt66=6;
                }
                break;
            case 35:
                {
                alt66=7;
                }
                break;
            case 52:
                {
                alt66=8;
                }
                break;
            case 53:
                {
                alt66=9;
                }
                break;
            case 54:
                {
                alt66=10;
                }
                break;
            case 55:
                {
                alt66=11;
                }
                break;
            case 36:
                {
                alt66=12;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("514:1: modifier : ( annotation | 'public' | 'protected' | 'private' | 'static' | 'abstract' | 'final' | 'native' | 'synchronized' | 'transient' | 'volatile' | 'strictfp' );", 66, 0, input);

                throw nvae;
            }

            switch (alt66) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:515:9: annotation
                    {
                    pushFollow(FOLLOW_annotation_in_modifier1936);
                    annotation();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:516:9: 'public'
                    {
                    match(input,31,FOLLOW_31_in_modifier1946); if (failed) return ;

                    }
                    break;
                case 3 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:517:9: 'protected'
                    {
                    match(input,32,FOLLOW_32_in_modifier1956); if (failed) return ;

                    }
                    break;
                case 4 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:518:9: 'private'
                    {
                    match(input,33,FOLLOW_33_in_modifier1966); if (failed) return ;

                    }
                    break;
                case 5 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:519:9: 'static'
                    {
                    match(input,28,FOLLOW_28_in_modifier1976); if (failed) return ;

                    }
                    break;
                case 6 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:520:9: 'abstract'
                    {
                    match(input,34,FOLLOW_34_in_modifier1986); if (failed) return ;

                    }
                    break;
                case 7 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:521:9: 'final'
                    {
                    match(input,35,FOLLOW_35_in_modifier1996); if (failed) return ;

                    }
                    break;
                case 8 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:522:9: 'native'
                    {
                    match(input,52,FOLLOW_52_in_modifier2006); if (failed) return ;

                    }
                    break;
                case 9 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:523:9: 'synchronized'
                    {
                    match(input,53,FOLLOW_53_in_modifier2016); if (failed) return ;

                    }
                    break;
                case 10 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:524:9: 'transient'
                    {
                    match(input,54,FOLLOW_54_in_modifier2026); if (failed) return ;

                    }
                    break;
                case 11 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:525:9: 'volatile'
                    {
                    match(input,55,FOLLOW_55_in_modifier2036); if (failed) return ;

                    }
                    break;
                case 12 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:526:9: 'strictfp'
                    {
                    match(input,36,FOLLOW_36_in_modifier2046); if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 49, modifier_StartIndex); }
        }
        return ;
    }
    // $ANTLR end modifier


    // $ANTLR start packageOrTypeName
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:529:1: packageOrTypeName : qualifiedName ;
    public final void packageOrTypeName() throws RecognitionException {
        int packageOrTypeName_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 50) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:530:5: ( qualifiedName )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:530:9: qualifiedName
            {
            pushFollow(FOLLOW_qualifiedName_in_packageOrTypeName2065);
            qualifiedName();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 50, packageOrTypeName_StartIndex); }
        }
        return ;
    }
    // $ANTLR end packageOrTypeName


    // $ANTLR start enumConstantName
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:533:1: enumConstantName : Identifier ;
    public final void enumConstantName() throws RecognitionException {
        int enumConstantName_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 51) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:534:5: ( Identifier )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:534:9: Identifier
            {
            match(input,Identifier,FOLLOW_Identifier_in_enumConstantName2084); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 51, enumConstantName_StartIndex); }
        }
        return ;
    }
    // $ANTLR end enumConstantName


    // $ANTLR start typeName
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:537:1: typeName : qualifiedName ;
    public final void typeName() throws RecognitionException {
        int typeName_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 52) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:538:5: ( qualifiedName )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:538:9: qualifiedName
            {
            pushFollow(FOLLOW_qualifiedName_in_typeName2103);
            qualifiedName();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 52, typeName_StartIndex); }
        }
        return ;
    }
    // $ANTLR end typeName

    public static class type_return extends ParserRuleReturnScope {
    };

    // $ANTLR start type
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:541:1: type : ( classOrInterfaceType ( '[' ']' )* | primitiveType ( '[' ']' )* );
    public final type_return type() throws RecognitionException {
        type_return retval = new type_return();
        retval.start = input.LT(1);
        int type_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 53) ) { return retval; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:542:2: ( classOrInterfaceType ( '[' ']' )* | primitiveType ( '[' ']' )* )
            int alt69=2;
            int LA69_0 = input.LA(1);

            if ( (LA69_0==Identifier) ) {
                alt69=1;
            }
            else if ( ((LA69_0>=56 && LA69_0<=63)) ) {
                alt69=2;
            }
            else {
                if (backtracking>0) {failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("541:1: type : ( classOrInterfaceType ( '[' ']' )* | primitiveType ( '[' ']' )* );", 69, 0, input);

                throw nvae;
            }
            switch (alt69) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:542:4: classOrInterfaceType ( '[' ']' )*
                    {
                    pushFollow(FOLLOW_classOrInterfaceType_in_type2117);
                    classOrInterfaceType();
                    _fsp--;
                    if (failed) return retval;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:542:25: ( '[' ']' )*
                    loop67:
                    do {
                        int alt67=2;
                        int LA67_0 = input.LA(1);

                        if ( (LA67_0==48) ) {
                            alt67=1;
                        }


                        switch (alt67) {
                    	case 1 :
                    	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:542:26: '[' ']'
                    	    {
                    	    match(input,48,FOLLOW_48_in_type2120); if (failed) return retval;
                    	    match(input,49,FOLLOW_49_in_type2122); if (failed) return retval;

                    	    }
                    	    break;

                    	default :
                    	    break loop67;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:543:4: primitiveType ( '[' ']' )*
                    {
                    pushFollow(FOLLOW_primitiveType_in_type2129);
                    primitiveType();
                    _fsp--;
                    if (failed) return retval;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:543:18: ( '[' ']' )*
                    loop68:
                    do {
                        int alt68=2;
                        int LA68_0 = input.LA(1);

                        if ( (LA68_0==48) ) {
                            alt68=1;
                        }


                        switch (alt68) {
                    	case 1 :
                    	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:543:19: '[' ']'
                    	    {
                    	    match(input,48,FOLLOW_48_in_type2132); if (failed) return retval;
                    	    match(input,49,FOLLOW_49_in_type2134); if (failed) return retval;

                    	    }
                    	    break;

                    	default :
                    	    break loop68;
                        }
                    } while (true);


                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 53, type_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end type


    // $ANTLR start classOrInterfaceType
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:546:1: classOrInterfaceType : name= Identifier ( typeArguments )? ( '.' Identifier ( typeArguments )? )* ;
    public final void classOrInterfaceType() throws RecognitionException {
        int classOrInterfaceType_StartIndex = input.index();
        Token name=null;

        try {
            if ( backtracking>0 && alreadyParsedRule(input, 54) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:547:2: (name= Identifier ( typeArguments )? ( '.' Identifier ( typeArguments )? )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:547:4: name= Identifier ( typeArguments )? ( '.' Identifier ( typeArguments )? )*
            {
            name=(Token)input.LT(1);
            match(input,Identifier,FOLLOW_Identifier_in_classOrInterfaceType2149); if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:547:20: ( typeArguments )?
            int alt70=2;
            int LA70_0 = input.LA(1);

            if ( (LA70_0==40) ) {
                int LA70_1 = input.LA(2);

                if ( (LA70_1==Identifier||(LA70_1>=56 && LA70_1<=64)) ) {
                    alt70=1;
                }
            }
            switch (alt70) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:547:21: typeArguments
                    {
                    pushFollow(FOLLOW_typeArguments_in_classOrInterfaceType2152);
                    typeArguments();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:547:37: ( '.' Identifier ( typeArguments )? )*
            loop72:
            do {
                int alt72=2;
                int LA72_0 = input.LA(1);

                if ( (LA72_0==29) ) {
                    alt72=1;
                }


                switch (alt72) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:547:38: '.' Identifier ( typeArguments )?
            	    {
            	    match(input,29,FOLLOW_29_in_classOrInterfaceType2157); if (failed) return ;
            	    match(input,Identifier,FOLLOW_Identifier_in_classOrInterfaceType2159); if (failed) return ;
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:547:53: ( typeArguments )?
            	    int alt71=2;
            	    int LA71_0 = input.LA(1);

            	    if ( (LA71_0==40) ) {
            	        int LA71_1 = input.LA(2);

            	        if ( (LA71_1==Identifier||(LA71_1>=56 && LA71_1<=64)) ) {
            	            alt71=1;
            	        }
            	    }
            	    switch (alt71) {
            	        case 1 :
            	            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:547:54: typeArguments
            	            {
            	            pushFollow(FOLLOW_typeArguments_in_classOrInterfaceType2162);
            	            typeArguments();
            	            _fsp--;
            	            if (failed) return ;

            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    break loop72;
                }
            } while (true);

            if ( backtracking==0 ) {
               lastTypeLine = name.getLine(); lastTypePos = name.getCharPositionInLine();
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 54, classOrInterfaceType_StartIndex); }
        }
        return ;
    }
    // $ANTLR end classOrInterfaceType


    // $ANTLR start primitiveType
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:550:1: primitiveType : ( 'boolean' | 'char' | 'byte' | 'short' | 'int' | 'long' | 'float' | 'double' );
    public final void primitiveType() throws RecognitionException {
        int primitiveType_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 55) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:551:5: ( 'boolean' | 'char' | 'byte' | 'short' | 'int' | 'long' | 'float' | 'double' )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:
            {
            if ( (input.LA(1)>=56 && input.LA(1)<=63) ) {
                input.consume();
                errorRecovery=false;failed=false;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_primitiveType0);    throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 55, primitiveType_StartIndex); }
        }
        return ;
    }
    // $ANTLR end primitiveType


    // $ANTLR start variableModifier
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:561:1: variableModifier : ( 'final' | annotation );
    public final void variableModifier() throws RecognitionException {
        int variableModifier_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 56) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:562:5: ( 'final' | annotation )
            int alt73=2;
            int LA73_0 = input.LA(1);

            if ( (LA73_0==35) ) {
                alt73=1;
            }
            else if ( (LA73_0==73) ) {
                alt73=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("561:1: variableModifier : ( 'final' | annotation );", 73, 0, input);

                throw nvae;
            }
            switch (alt73) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:562:9: 'final'
                    {
                    match(input,35,FOLLOW_35_in_variableModifier2275); if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:563:9: annotation
                    {
                    pushFollow(FOLLOW_annotation_in_variableModifier2285);
                    annotation();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 56, variableModifier_StartIndex); }
        }
        return ;
    }
    // $ANTLR end variableModifier


    // $ANTLR start typeArguments
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:566:1: typeArguments : '<' typeArgument ( ',' typeArgument )* '>' ;
    public final void typeArguments() throws RecognitionException {
        int typeArguments_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 57) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:567:5: ( '<' typeArgument ( ',' typeArgument )* '>' )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:567:9: '<' typeArgument ( ',' typeArgument )* '>'
            {
            match(input,40,FOLLOW_40_in_typeArguments2304); if (failed) return ;
            pushFollow(FOLLOW_typeArgument_in_typeArguments2306);
            typeArgument();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:567:26: ( ',' typeArgument )*
            loop74:
            do {
                int alt74=2;
                int LA74_0 = input.LA(1);

                if ( (LA74_0==41) ) {
                    alt74=1;
                }


                switch (alt74) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:567:27: ',' typeArgument
            	    {
            	    match(input,41,FOLLOW_41_in_typeArguments2309); if (failed) return ;
            	    pushFollow(FOLLOW_typeArgument_in_typeArguments2311);
            	    typeArgument();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop74;
                }
            } while (true);

            match(input,42,FOLLOW_42_in_typeArguments2315); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 57, typeArguments_StartIndex); }
        }
        return ;
    }
    // $ANTLR end typeArguments


    // $ANTLR start typeArgument
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:570:1: typeArgument : ( type | '?' ( ( 'extends' | 'super' ) type )? );
    public final void typeArgument() throws RecognitionException {
        int typeArgument_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 58) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:571:5: ( type | '?' ( ( 'extends' | 'super' ) type )? )
            int alt76=2;
            int LA76_0 = input.LA(1);

            if ( (LA76_0==Identifier||(LA76_0>=56 && LA76_0<=63)) ) {
                alt76=1;
            }
            else if ( (LA76_0==64) ) {
                alt76=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("570:1: typeArgument : ( type | '?' ( ( 'extends' | 'super' ) type )? );", 76, 0, input);

                throw nvae;
            }
            switch (alt76) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:571:9: type
                    {
                    pushFollow(FOLLOW_type_in_typeArgument2338);
                    type();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:572:9: '?' ( ( 'extends' | 'super' ) type )?
                    {
                    match(input,64,FOLLOW_64_in_typeArgument2348); if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:572:13: ( ( 'extends' | 'super' ) type )?
                    int alt75=2;
                    int LA75_0 = input.LA(1);

                    if ( (LA75_0==38||LA75_0==65) ) {
                        alt75=1;
                    }
                    switch (alt75) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:572:14: ( 'extends' | 'super' ) type
                            {
                            if ( input.LA(1)==38||input.LA(1)==65 ) {
                                input.consume();
                                errorRecovery=false;failed=false;
                            }
                            else {
                                if (backtracking>0) {failed=true; return ;}
                                MismatchedSetException mse =
                                    new MismatchedSetException(null,input);
                                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_typeArgument2351);    throw mse;
                            }

                            pushFollow(FOLLOW_type_in_typeArgument2359);
                            type();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 58, typeArgument_StartIndex); }
        }
        return ;
    }
    // $ANTLR end typeArgument


    // $ANTLR start qualifiedNameList
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:575:1: qualifiedNameList : qualifiedName ( ',' qualifiedName )* ;
    public final void qualifiedNameList() throws RecognitionException {
        int qualifiedNameList_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 59) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:576:5: ( qualifiedName ( ',' qualifiedName )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:576:9: qualifiedName ( ',' qualifiedName )*
            {
            pushFollow(FOLLOW_qualifiedName_in_qualifiedNameList2384);
            qualifiedName();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:576:23: ( ',' qualifiedName )*
            loop77:
            do {
                int alt77=2;
                int LA77_0 = input.LA(1);

                if ( (LA77_0==41) ) {
                    alt77=1;
                }


                switch (alt77) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:576:24: ',' qualifiedName
            	    {
            	    match(input,41,FOLLOW_41_in_qualifiedNameList2387); if (failed) return ;
            	    pushFollow(FOLLOW_qualifiedName_in_qualifiedNameList2389);
            	    qualifiedName();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop77;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 59, qualifiedNameList_StartIndex); }
        }
        return ;
    }
    // $ANTLR end qualifiedNameList


    // $ANTLR start formalParameters
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:579:1: formalParameters : '(' ( formalParameterDecls )? ')' ;
    public final void formalParameters() throws RecognitionException {
        int formalParameters_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 60) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:580:5: ( '(' ( formalParameterDecls )? ')' )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:580:9: '(' ( formalParameterDecls )? ')'
            {
            match(input,66,FOLLOW_66_in_formalParameters2410); if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:580:13: ( formalParameterDecls )?
            int alt78=2;
            int LA78_0 = input.LA(1);

            if ( (LA78_0==Identifier||LA78_0==35||(LA78_0>=56 && LA78_0<=63)||LA78_0==73) ) {
                alt78=1;
            }
            switch (alt78) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: formalParameterDecls
                    {
                    pushFollow(FOLLOW_formalParameterDecls_in_formalParameters2412);
                    formalParameterDecls();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            match(input,67,FOLLOW_67_in_formalParameters2415); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 60, formalParameters_StartIndex); }
        }
        return ;
    }
    // $ANTLR end formalParameters


    // $ANTLR start formalParameterDecls
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:583:1: formalParameterDecls : variableModifiers type formalParameterDeclsRest ;
    public final void formalParameterDecls() throws RecognitionException {
        int formalParameterDecls_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 61) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:584:5: ( variableModifiers type formalParameterDeclsRest )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:584:9: variableModifiers type formalParameterDeclsRest
            {
            pushFollow(FOLLOW_variableModifiers_in_formalParameterDecls2438);
            variableModifiers();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_type_in_formalParameterDecls2440);
            type();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_formalParameterDeclsRest_in_formalParameterDecls2442);
            formalParameterDeclsRest();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 61, formalParameterDecls_StartIndex); }
        }
        return ;
    }
    // $ANTLR end formalParameterDecls


    // $ANTLR start formalParameterDeclsRest
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:587:1: formalParameterDeclsRest : ( variableDeclaratorId ( ',' formalParameterDecls )? | '...' variableDeclaratorId );
    public final void formalParameterDeclsRest() throws RecognitionException {
        int formalParameterDeclsRest_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 62) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:588:5: ( variableDeclaratorId ( ',' formalParameterDecls )? | '...' variableDeclaratorId )
            int alt80=2;
            int LA80_0 = input.LA(1);

            if ( (LA80_0==Identifier) ) {
                alt80=1;
            }
            else if ( (LA80_0==68) ) {
                alt80=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("587:1: formalParameterDeclsRest : ( variableDeclaratorId ( ',' formalParameterDecls )? | '...' variableDeclaratorId );", 80, 0, input);

                throw nvae;
            }
            switch (alt80) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:588:9: variableDeclaratorId ( ',' formalParameterDecls )?
                    {
                    pushFollow(FOLLOW_variableDeclaratorId_in_formalParameterDeclsRest2465);
                    variableDeclaratorId();
                    _fsp--;
                    if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:588:30: ( ',' formalParameterDecls )?
                    int alt79=2;
                    int LA79_0 = input.LA(1);

                    if ( (LA79_0==41) ) {
                        alt79=1;
                    }
                    switch (alt79) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:588:31: ',' formalParameterDecls
                            {
                            match(input,41,FOLLOW_41_in_formalParameterDeclsRest2468); if (failed) return ;
                            pushFollow(FOLLOW_formalParameterDecls_in_formalParameterDeclsRest2470);
                            formalParameterDecls();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:589:9: '...' variableDeclaratorId
                    {
                    match(input,68,FOLLOW_68_in_formalParameterDeclsRest2482); if (failed) return ;
                    pushFollow(FOLLOW_variableDeclaratorId_in_formalParameterDeclsRest2484);
                    variableDeclaratorId();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 62, formalParameterDeclsRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end formalParameterDeclsRest


    // $ANTLR start methodBody
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:592:1: methodBody : block ;
    public final void methodBody() throws RecognitionException {
        int methodBody_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 63) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:593:5: ( block )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:593:9: block
            {
            pushFollow(FOLLOW_block_in_methodBody2507);
            block();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 63, methodBody_StartIndex); }
        }
        return ;
    }
    // $ANTLR end methodBody


    // $ANTLR start constructorBody
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:596:1: constructorBody : '{' ( explicitConstructorInvocation )? ( blockStatement )* '}' ;
    public final void constructorBody() throws RecognitionException {
        int constructorBody_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 64) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:597:5: ( '{' ( explicitConstructorInvocation )? ( blockStatement )* '}' )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:597:9: '{' ( explicitConstructorInvocation )? ( blockStatement )* '}'
            {
            match(input,44,FOLLOW_44_in_constructorBody2526); if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:597:13: ( explicitConstructorInvocation )?
            int alt81=2;
            switch ( input.LA(1) ) {
                case 40:
                    {
                    alt81=1;
                    }
                    break;
                case 69:
                    {
                    switch ( input.LA(2) ) {
                        case 29:
                            {
                            int LA81_47 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                        case 48:
                            {
                            int LA81_48 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                        case 66:
                            {
                            int LA81_49 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                    }

                    }
                    break;
                case 65:
                    {
                    int LA81_3 = input.LA(2);

                    if ( (LA81_3==66) ) {
                        int LA81_73 = input.LA(3);

                        if ( (synpred113()) ) {
                            alt81=1;
                        }
                    }
                    else if ( (LA81_3==29) ) {
                        int LA81_74 = input.LA(3);

                        if ( (synpred113()) ) {
                            alt81=1;
                        }
                    }
                    }
                    break;
                case 66:
                    {
                    switch ( input.LA(2) ) {
                        case 105:
                            {
                            int LA81_75 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                        case 106:
                            {
                            int LA81_76 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                        case 109:
                            {
                            int LA81_77 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                        case 110:
                            {
                            int LA81_78 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                        case 111:
                            {
                            int LA81_79 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                        case 112:
                            {
                            int LA81_80 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                        case 66:
                            {
                            int LA81_81 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                        case 69:
                            {
                            int LA81_82 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                        case 65:
                            {
                            int LA81_83 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                        case HexLiteral:
                        case OctalLiteral:
                        case DecimalLiteral:
                            {
                            int LA81_84 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                        case FloatingPointLiteral:
                            {
                            int LA81_85 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                        case CharacterLiteral:
                            {
                            int LA81_86 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                        case StringLiteral:
                            {
                            int LA81_87 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                        case 71:
                        case 72:
                            {
                            int LA81_88 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                        case 70:
                            {
                            int LA81_89 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                        case 113:
                            {
                            int LA81_90 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                        case Identifier:
                            {
                            int LA81_91 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                        case 56:
                        case 57:
                        case 58:
                        case 59:
                        case 60:
                        case 61:
                        case 62:
                        case 63:
                            {
                            int LA81_92 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                        case 47:
                            {
                            int LA81_93 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                    }

                    }
                    break;
                case HexLiteral:
                case OctalLiteral:
                case DecimalLiteral:
                    {
                    int LA81_5 = input.LA(2);

                    if ( (LA81_5==29) ) {
                        int LA81_94 = input.LA(3);

                        if ( (synpred113()) ) {
                            alt81=1;
                        }
                    }
                    }
                    break;
                case FloatingPointLiteral:
                    {
                    int LA81_6 = input.LA(2);

                    if ( (LA81_6==29) ) {
                        int LA81_119 = input.LA(3);

                        if ( (synpred113()) ) {
                            alt81=1;
                        }
                    }
                    }
                    break;
                case CharacterLiteral:
                    {
                    int LA81_7 = input.LA(2);

                    if ( (LA81_7==29) ) {
                        int LA81_144 = input.LA(3);

                        if ( (synpred113()) ) {
                            alt81=1;
                        }
                    }
                    }
                    break;
                case StringLiteral:
                    {
                    int LA81_8 = input.LA(2);

                    if ( (LA81_8==29) ) {
                        int LA81_169 = input.LA(3);

                        if ( (synpred113()) ) {
                            alt81=1;
                        }
                    }
                    }
                    break;
                case 71:
                case 72:
                    {
                    int LA81_9 = input.LA(2);

                    if ( (LA81_9==29) ) {
                        int LA81_194 = input.LA(3);

                        if ( (synpred113()) ) {
                            alt81=1;
                        }
                    }
                    }
                    break;
                case 70:
                    {
                    int LA81_10 = input.LA(2);

                    if ( (LA81_10==29) ) {
                        int LA81_219 = input.LA(3);

                        if ( (synpred113()) ) {
                            alt81=1;
                        }
                    }
                    }
                    break;
                case 113:
                    {
                    switch ( input.LA(2) ) {
                        case 40:
                            {
                            int LA81_244 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                        case Identifier:
                            {
                            int LA81_245 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                        case 56:
                        case 57:
                        case 58:
                        case 59:
                        case 60:
                        case 61:
                        case 62:
                        case 63:
                            {
                            int LA81_246 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                    }

                    }
                    break;
                case Identifier:
                    {
                    switch ( input.LA(2) ) {
                        case 29:
                            {
                            int LA81_249 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                        case 48:
                            {
                            int LA81_250 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                        case 66:
                            {
                            int LA81_252 = input.LA(3);

                            if ( (synpred113()) ) {
                                alt81=1;
                            }
                            }
                            break;
                    }

                    }
                    break;
                case 56:
                case 57:
                case 58:
                case 59:
                case 60:
                case 61:
                case 62:
                case 63:
                    {
                    int LA81_13 = input.LA(2);

                    if ( (LA81_13==48) ) {
                        int LA81_275 = input.LA(3);

                        if ( (synpred113()) ) {
                            alt81=1;
                        }
                    }
                    else if ( (LA81_13==29) ) {
                        int LA81_276 = input.LA(3);

                        if ( (synpred113()) ) {
                            alt81=1;
                        }
                    }
                    }
                    break;
                case 47:
                    {
                    int LA81_14 = input.LA(2);

                    if ( (LA81_14==29) ) {
                        int LA81_278 = input.LA(3);

                        if ( (synpred113()) ) {
                            alt81=1;
                        }
                    }
                    }
                    break;
            }

            switch (alt81) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: explicitConstructorInvocation
                    {
                    pushFollow(FOLLOW_explicitConstructorInvocation_in_constructorBody2528);
                    explicitConstructorInvocation();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:597:44: ( blockStatement )*
            loop82:
            do {
                int alt82=2;
                int LA82_0 = input.LA(1);

                if ( ((LA82_0>=Identifier && LA82_0<=ASSERT)||LA82_0==26||LA82_0==28||(LA82_0>=31 && LA82_0<=37)||LA82_0==44||(LA82_0>=46 && LA82_0<=47)||LA82_0==53||(LA82_0>=56 && LA82_0<=63)||(LA82_0>=65 && LA82_0<=66)||(LA82_0>=69 && LA82_0<=73)||LA82_0==76||(LA82_0>=78 && LA82_0<=81)||(LA82_0>=83 && LA82_0<=87)||(LA82_0>=105 && LA82_0<=106)||(LA82_0>=109 && LA82_0<=113)) ) {
                    alt82=1;
                }


                switch (alt82) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: blockStatement
            	    {
            	    pushFollow(FOLLOW_blockStatement_in_constructorBody2531);
            	    blockStatement();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop82;
                }
            } while (true);

            match(input,45,FOLLOW_45_in_constructorBody2534); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 64, constructorBody_StartIndex); }
        }
        return ;
    }
    // $ANTLR end constructorBody


    // $ANTLR start explicitConstructorInvocation
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:600:1: explicitConstructorInvocation : ( ( nonWildcardTypeArguments )? ( 'this' | t1= 'super' ) arguments ';' | primary '.' ( nonWildcardTypeArguments )? t1= 'super' arguments ';' );
    public final void explicitConstructorInvocation() throws RecognitionException {
        int explicitConstructorInvocation_StartIndex = input.index();
        Token t1=null;

        try {
            if ( backtracking>0 && alreadyParsedRule(input, 65) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:601:5: ( ( nonWildcardTypeArguments )? ( 'this' | t1= 'super' ) arguments ';' | primary '.' ( nonWildcardTypeArguments )? t1= 'super' arguments ';' )
            int alt86=2;
            switch ( input.LA(1) ) {
            case 40:
                {
                alt86=1;
                }
                break;
            case 69:
                {
                int LA86_2 = input.LA(2);

                if ( (LA86_2==66) ) {
                    int LA86_15 = input.LA(3);

                    if ( (synpred117()) ) {
                        alt86=1;
                    }
                    else if ( (true) ) {
                        alt86=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("600:1: explicitConstructorInvocation : ( ( nonWildcardTypeArguments )? ( 'this' | t1= 'super' ) arguments ';' | primary '.' ( nonWildcardTypeArguments )? t1= 'super' arguments ';' );", 86, 15, input);

                        throw nvae;
                    }
                }
                else if ( (LA86_2==29||LA86_2==48) ) {
                    alt86=2;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("600:1: explicitConstructorInvocation : ( ( nonWildcardTypeArguments )? ( 'this' | t1= 'super' ) arguments ';' | primary '.' ( nonWildcardTypeArguments )? t1= 'super' arguments ';' );", 86, 2, input);

                    throw nvae;
                }
                }
                break;
            case 65:
                {
                int LA86_3 = input.LA(2);

                if ( (LA86_3==66) ) {
                    int LA86_18 = input.LA(3);

                    if ( (synpred117()) ) {
                        alt86=1;
                    }
                    else if ( (true) ) {
                        alt86=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("600:1: explicitConstructorInvocation : ( ( nonWildcardTypeArguments )? ( 'this' | t1= 'super' ) arguments ';' | primary '.' ( nonWildcardTypeArguments )? t1= 'super' arguments ';' );", 86, 18, input);

                        throw nvae;
                    }
                }
                else if ( (LA86_3==29) ) {
                    alt86=2;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("600:1: explicitConstructorInvocation : ( ( nonWildcardTypeArguments )? ( 'this' | t1= 'super' ) arguments ';' | primary '.' ( nonWildcardTypeArguments )? t1= 'super' arguments ';' );", 86, 3, input);

                    throw nvae;
                }
                }
                break;
            case Identifier:
            case FloatingPointLiteral:
            case CharacterLiteral:
            case StringLiteral:
            case HexLiteral:
            case OctalLiteral:
            case DecimalLiteral:
            case 47:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 66:
            case 70:
            case 71:
            case 72:
            case 113:
                {
                alt86=2;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("600:1: explicitConstructorInvocation : ( ( nonWildcardTypeArguments )? ( 'this' | t1= 'super' ) arguments ';' | primary '.' ( nonWildcardTypeArguments )? t1= 'super' arguments ';' );", 86, 0, input);

                throw nvae;
            }

            switch (alt86) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:601:9: ( nonWildcardTypeArguments )? ( 'this' | t1= 'super' ) arguments ';'
                    {
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:601:9: ( nonWildcardTypeArguments )?
                    int alt83=2;
                    int LA83_0 = input.LA(1);

                    if ( (LA83_0==40) ) {
                        alt83=1;
                    }
                    switch (alt83) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:601:10: nonWildcardTypeArguments
                            {
                            pushFollow(FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation2554);
                            nonWildcardTypeArguments();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }

                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:601:37: ( 'this' | t1= 'super' )
                    int alt84=2;
                    int LA84_0 = input.LA(1);

                    if ( (LA84_0==69) ) {
                        alt84=1;
                    }
                    else if ( (LA84_0==65) ) {
                        alt84=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("601:37: ( 'this' | t1= 'super' )", 84, 0, input);

                        throw nvae;
                    }
                    switch (alt84) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:601:38: 'this'
                            {
                            match(input,69,FOLLOW_69_in_explicitConstructorInvocation2559); if (failed) return ;

                            }
                            break;
                        case 2 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:601:47: t1= 'super'
                            {
                            t1=(Token)input.LT(1);
                            match(input,65,FOLLOW_65_in_explicitConstructorInvocation2565); if (failed) return ;
                            if ( backtracking==0 ) {
                               addSuperConstructorInvocation(t1.getLine()); 
                            }

                            }
                            break;

                    }

                    pushFollow(FOLLOW_arguments_in_explicitConstructorInvocation2570);
                    arguments();
                    _fsp--;
                    if (failed) return ;
                    match(input,26,FOLLOW_26_in_explicitConstructorInvocation2572); if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:602:9: primary '.' ( nonWildcardTypeArguments )? t1= 'super' arguments ';'
                    {
                    pushFollow(FOLLOW_primary_in_explicitConstructorInvocation2582);
                    primary();
                    _fsp--;
                    if (failed) return ;
                    match(input,29,FOLLOW_29_in_explicitConstructorInvocation2584); if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:602:21: ( nonWildcardTypeArguments )?
                    int alt85=2;
                    int LA85_0 = input.LA(1);

                    if ( (LA85_0==40) ) {
                        alt85=1;
                    }
                    switch (alt85) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: nonWildcardTypeArguments
                            {
                            pushFollow(FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation2586);
                            nonWildcardTypeArguments();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }

                    t1=(Token)input.LT(1);
                    match(input,65,FOLLOW_65_in_explicitConstructorInvocation2591); if (failed) return ;
                    if ( backtracking==0 ) {
                       addSuperConstructorInvocation(t1.getLine()); 
                    }
                    pushFollow(FOLLOW_arguments_in_explicitConstructorInvocation2595);
                    arguments();
                    _fsp--;
                    if (failed) return ;
                    match(input,26,FOLLOW_26_in_explicitConstructorInvocation2597); if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 65, explicitConstructorInvocation_StartIndex); }
        }
        return ;
    }
    // $ANTLR end explicitConstructorInvocation

    public static class qualifiedName_return extends ParserRuleReturnScope {
    };

    // $ANTLR start qualifiedName
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:606:1: qualifiedName : Identifier ( '.' Identifier )* ;
    public final qualifiedName_return qualifiedName() throws RecognitionException {
        qualifiedName_return retval = new qualifiedName_return();
        retval.start = input.LT(1);
        int qualifiedName_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 66) ) { return retval; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:607:5: ( Identifier ( '.' Identifier )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:607:9: Identifier ( '.' Identifier )*
            {
            match(input,Identifier,FOLLOW_Identifier_in_qualifiedName2617); if (failed) return retval;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:607:20: ( '.' Identifier )*
            loop87:
            do {
                int alt87=2;
                int LA87_0 = input.LA(1);

                if ( (LA87_0==29) ) {
                    int LA87_2 = input.LA(2);

                    if ( (LA87_2==Identifier) ) {
                        alt87=1;
                    }


                }


                switch (alt87) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:607:21: '.' Identifier
            	    {
            	    match(input,29,FOLLOW_29_in_qualifiedName2620); if (failed) return retval;
            	    match(input,Identifier,FOLLOW_Identifier_in_qualifiedName2622); if (failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop87;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 66, qualifiedName_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end qualifiedName


    // $ANTLR start literal
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:610:1: literal : ( integerLiteral | FloatingPointLiteral | CharacterLiteral | StringLiteral | booleanLiteral | 'null' );
    public final void literal() throws RecognitionException {
        int literal_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 67) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:611:5: ( integerLiteral | FloatingPointLiteral | CharacterLiteral | StringLiteral | booleanLiteral | 'null' )
            int alt88=6;
            switch ( input.LA(1) ) {
            case HexLiteral:
            case OctalLiteral:
            case DecimalLiteral:
                {
                alt88=1;
                }
                break;
            case FloatingPointLiteral:
                {
                alt88=2;
                }
                break;
            case CharacterLiteral:
                {
                alt88=3;
                }
                break;
            case StringLiteral:
                {
                alt88=4;
                }
                break;
            case 71:
            case 72:
                {
                alt88=5;
                }
                break;
            case 70:
                {
                alt88=6;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("610:1: literal : ( integerLiteral | FloatingPointLiteral | CharacterLiteral | StringLiteral | booleanLiteral | 'null' );", 88, 0, input);

                throw nvae;
            }

            switch (alt88) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:611:9: integerLiteral
                    {
                    pushFollow(FOLLOW_integerLiteral_in_literal2648);
                    integerLiteral();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:612:9: FloatingPointLiteral
                    {
                    match(input,FloatingPointLiteral,FOLLOW_FloatingPointLiteral_in_literal2658); if (failed) return ;

                    }
                    break;
                case 3 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:613:9: CharacterLiteral
                    {
                    match(input,CharacterLiteral,FOLLOW_CharacterLiteral_in_literal2668); if (failed) return ;

                    }
                    break;
                case 4 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:614:9: StringLiteral
                    {
                    match(input,StringLiteral,FOLLOW_StringLiteral_in_literal2678); if (failed) return ;

                    }
                    break;
                case 5 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:615:9: booleanLiteral
                    {
                    pushFollow(FOLLOW_booleanLiteral_in_literal2688);
                    booleanLiteral();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 6 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:616:9: 'null'
                    {
                    match(input,70,FOLLOW_70_in_literal2698); if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 67, literal_StartIndex); }
        }
        return ;
    }
    // $ANTLR end literal


    // $ANTLR start integerLiteral
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:619:1: integerLiteral : ( HexLiteral | OctalLiteral | DecimalLiteral );
    public final void integerLiteral() throws RecognitionException {
        int integerLiteral_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 68) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:620:5: ( HexLiteral | OctalLiteral | DecimalLiteral )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:
            {
            if ( (input.LA(1)>=HexLiteral && input.LA(1)<=DecimalLiteral) ) {
                input.consume();
                errorRecovery=false;failed=false;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_integerLiteral0);    throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 68, integerLiteral_StartIndex); }
        }
        return ;
    }
    // $ANTLR end integerLiteral


    // $ANTLR start booleanLiteral
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:625:1: booleanLiteral : ( 'true' | 'false' );
    public final void booleanLiteral() throws RecognitionException {
        int booleanLiteral_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 69) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:626:5: ( 'true' | 'false' )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:
            {
            if ( (input.LA(1)>=71 && input.LA(1)<=72) ) {
                input.consume();
                errorRecovery=false;failed=false;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_booleanLiteral0);    throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 69, booleanLiteral_StartIndex); }
        }
        return ;
    }
    // $ANTLR end booleanLiteral


    // $ANTLR start annotations
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:632:1: annotations : ( annotation )+ ;
    public final void annotations() throws RecognitionException {
        int annotations_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 70) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:633:5: ( ( annotation )+ )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:633:9: ( annotation )+
            {
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:633:9: ( annotation )+
            int cnt89=0;
            loop89:
            do {
                int alt89=2;
                int LA89_0 = input.LA(1);

                if ( (LA89_0==73) ) {
                    int LA89_2 = input.LA(2);

                    if ( (LA89_2==Identifier) ) {
                        int LA89_16 = input.LA(3);

                        if ( (synpred128()) ) {
                            alt89=1;
                        }


                    }


                }


                switch (alt89) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: annotation
            	    {
            	    pushFollow(FOLLOW_annotation_in_annotations2787);
            	    annotation();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    if ( cnt89 >= 1 ) break loop89;
            	    if (backtracking>0) {failed=true; return ;}
                        EarlyExitException eee =
                            new EarlyExitException(89, input);
                        throw eee;
                }
                cnt89++;
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 70, annotations_StartIndex); }
        }
        return ;
    }
    // $ANTLR end annotations


    // $ANTLR start annotation
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:636:1: annotation : '@' annotationName ( '(' ( elementValuePairs | elementValue )? ')' )? ;
    public final void annotation() throws RecognitionException {
        int annotation_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 71) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:637:5: ( '@' annotationName ( '(' ( elementValuePairs | elementValue )? ')' )? )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:637:9: '@' annotationName ( '(' ( elementValuePairs | elementValue )? ')' )?
            {
            match(input,73,FOLLOW_73_in_annotation2807); if (failed) return ;
            pushFollow(FOLLOW_annotationName_in_annotation2809);
            annotationName();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:637:28: ( '(' ( elementValuePairs | elementValue )? ')' )?
            int alt91=2;
            int LA91_0 = input.LA(1);

            if ( (LA91_0==66) ) {
                alt91=1;
            }
            switch (alt91) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:637:30: '(' ( elementValuePairs | elementValue )? ')'
                    {
                    match(input,66,FOLLOW_66_in_annotation2813); if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:637:34: ( elementValuePairs | elementValue )?
                    int alt90=3;
                    int LA90_0 = input.LA(1);

                    if ( (LA90_0==Identifier) ) {
                        int LA90_1 = input.LA(2);

                        if ( (LA90_1==51) ) {
                            alt90=1;
                        }
                        else if ( ((LA90_1>=29 && LA90_1<=30)||LA90_1==40||(LA90_1>=42 && LA90_1<=43)||LA90_1==48||LA90_1==64||(LA90_1>=66 && LA90_1<=67)||(LA90_1>=98 && LA90_1<=110)) ) {
                            alt90=2;
                        }
                    }
                    else if ( ((LA90_0>=FloatingPointLiteral && LA90_0<=DecimalLiteral)||LA90_0==44||LA90_0==47||(LA90_0>=56 && LA90_0<=63)||(LA90_0>=65 && LA90_0<=66)||(LA90_0>=69 && LA90_0<=73)||(LA90_0>=105 && LA90_0<=106)||(LA90_0>=109 && LA90_0<=113)) ) {
                        alt90=2;
                    }
                    switch (alt90) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:637:36: elementValuePairs
                            {
                            pushFollow(FOLLOW_elementValuePairs_in_annotation2817);
                            elementValuePairs();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;
                        case 2 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:637:56: elementValue
                            {
                            pushFollow(FOLLOW_elementValue_in_annotation2821);
                            elementValue();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }

                    match(input,67,FOLLOW_67_in_annotation2826); if (failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 71, annotation_StartIndex); }
        }
        return ;
    }
    // $ANTLR end annotation


    // $ANTLR start annotationName
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:640:1: annotationName : name= Identifier ( '.' name= Identifier )* ;
    public final void annotationName() throws RecognitionException {
        int annotationName_StartIndex = input.index();
        Token name=null;

        try {
            if ( backtracking>0 && alreadyParsedRule(input, 72) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:641:5: (name= Identifier ( '.' name= Identifier )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:641:7: name= Identifier ( '.' name= Identifier )*
            {
            name=(Token)input.LT(1);
            match(input,Identifier,FOLLOW_Identifier_in_annotationName2852); if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:641:23: ( '.' name= Identifier )*
            loop92:
            do {
                int alt92=2;
                int LA92_0 = input.LA(1);

                if ( (LA92_0==29) ) {
                    alt92=1;
                }


                switch (alt92) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:641:24: '.' name= Identifier
            	    {
            	    match(input,29,FOLLOW_29_in_annotationName2855); if (failed) return ;
            	    name=(Token)input.LT(1);
            	    match(input,Identifier,FOLLOW_Identifier_in_annotationName2859); if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop92;
                }
            } while (true);

            if ( backtracking==0 ) {

                  	if (classDepth == 1 && name.getText().equals("Override")) {
                  		overrideAnnotationsLines.add(name.getLine());
                  	}
                  	if (classDepth == 1) {
                  		currentAnnotations += name.getText() + " ";
                  	}
                  
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 72, annotationName_StartIndex); }
        }
        return ;
    }
    // $ANTLR end annotationName


    // $ANTLR start elementValuePairs
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:651:1: elementValuePairs : elementValuePair ( ',' elementValuePair )* ;
    public final void elementValuePairs() throws RecognitionException {
        int elementValuePairs_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 73) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:652:5: ( elementValuePair ( ',' elementValuePair )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:652:9: elementValuePair ( ',' elementValuePair )*
            {
            pushFollow(FOLLOW_elementValuePair_in_elementValuePairs2883);
            elementValuePair();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:652:26: ( ',' elementValuePair )*
            loop93:
            do {
                int alt93=2;
                int LA93_0 = input.LA(1);

                if ( (LA93_0==41) ) {
                    alt93=1;
                }


                switch (alt93) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:652:27: ',' elementValuePair
            	    {
            	    match(input,41,FOLLOW_41_in_elementValuePairs2886); if (failed) return ;
            	    pushFollow(FOLLOW_elementValuePair_in_elementValuePairs2888);
            	    elementValuePair();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop93;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 73, elementValuePairs_StartIndex); }
        }
        return ;
    }
    // $ANTLR end elementValuePairs


    // $ANTLR start elementValuePair
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:655:1: elementValuePair : Identifier '=' elementValue ;
    public final void elementValuePair() throws RecognitionException {
        int elementValuePair_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 74) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:656:5: ( Identifier '=' elementValue )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:656:9: Identifier '=' elementValue
            {
            match(input,Identifier,FOLLOW_Identifier_in_elementValuePair2909); if (failed) return ;
            match(input,51,FOLLOW_51_in_elementValuePair2911); if (failed) return ;
            pushFollow(FOLLOW_elementValue_in_elementValuePair2913);
            elementValue();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 74, elementValuePair_StartIndex); }
        }
        return ;
    }
    // $ANTLR end elementValuePair


    // $ANTLR start elementValue
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:659:1: elementValue : ( conditionalExpression | annotation | elementValueArrayInitializer );
    public final void elementValue() throws RecognitionException {
        int elementValue_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 75) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:660:5: ( conditionalExpression | annotation | elementValueArrayInitializer )
            int alt94=3;
            switch ( input.LA(1) ) {
            case Identifier:
            case FloatingPointLiteral:
            case CharacterLiteral:
            case StringLiteral:
            case HexLiteral:
            case OctalLiteral:
            case DecimalLiteral:
            case 47:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 65:
            case 66:
            case 69:
            case 70:
            case 71:
            case 72:
            case 105:
            case 106:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
                {
                alt94=1;
                }
                break;
            case 73:
                {
                alt94=2;
                }
                break;
            case 44:
                {
                alt94=3;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("659:1: elementValue : ( conditionalExpression | annotation | elementValueArrayInitializer );", 94, 0, input);

                throw nvae;
            }

            switch (alt94) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:660:9: conditionalExpression
                    {
                    pushFollow(FOLLOW_conditionalExpression_in_elementValue2936);
                    conditionalExpression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:661:9: annotation
                    {
                    pushFollow(FOLLOW_annotation_in_elementValue2946);
                    annotation();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 3 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:662:9: elementValueArrayInitializer
                    {
                    pushFollow(FOLLOW_elementValueArrayInitializer_in_elementValue2956);
                    elementValueArrayInitializer();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 75, elementValue_StartIndex); }
        }
        return ;
    }
    // $ANTLR end elementValue


    // $ANTLR start elementValueArrayInitializer
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:665:1: elementValueArrayInitializer : '{' ( elementValue ( ',' elementValue )* )? ( ',' )? '}' ;
    public final void elementValueArrayInitializer() throws RecognitionException {
        int elementValueArrayInitializer_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 76) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:666:5: ( '{' ( elementValue ( ',' elementValue )* )? ( ',' )? '}' )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:666:9: '{' ( elementValue ( ',' elementValue )* )? ( ',' )? '}'
            {
            match(input,44,FOLLOW_44_in_elementValueArrayInitializer2979); if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:666:13: ( elementValue ( ',' elementValue )* )?
            int alt96=2;
            int LA96_0 = input.LA(1);

            if ( (LA96_0==Identifier||(LA96_0>=FloatingPointLiteral && LA96_0<=DecimalLiteral)||LA96_0==44||LA96_0==47||(LA96_0>=56 && LA96_0<=63)||(LA96_0>=65 && LA96_0<=66)||(LA96_0>=69 && LA96_0<=73)||(LA96_0>=105 && LA96_0<=106)||(LA96_0>=109 && LA96_0<=113)) ) {
                alt96=1;
            }
            switch (alt96) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:666:14: elementValue ( ',' elementValue )*
                    {
                    pushFollow(FOLLOW_elementValue_in_elementValueArrayInitializer2982);
                    elementValue();
                    _fsp--;
                    if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:666:27: ( ',' elementValue )*
                    loop95:
                    do {
                        int alt95=2;
                        int LA95_0 = input.LA(1);

                        if ( (LA95_0==41) ) {
                            int LA95_1 = input.LA(2);

                            if ( (LA95_1==Identifier||(LA95_1>=FloatingPointLiteral && LA95_1<=DecimalLiteral)||LA95_1==44||LA95_1==47||(LA95_1>=56 && LA95_1<=63)||(LA95_1>=65 && LA95_1<=66)||(LA95_1>=69 && LA95_1<=73)||(LA95_1>=105 && LA95_1<=106)||(LA95_1>=109 && LA95_1<=113)) ) {
                                alt95=1;
                            }


                        }


                        switch (alt95) {
                    	case 1 :
                    	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:666:28: ',' elementValue
                    	    {
                    	    match(input,41,FOLLOW_41_in_elementValueArrayInitializer2985); if (failed) return ;
                    	    pushFollow(FOLLOW_elementValue_in_elementValueArrayInitializer2987);
                    	    elementValue();
                    	    _fsp--;
                    	    if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop95;
                        }
                    } while (true);


                    }
                    break;

            }

            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:666:49: ( ',' )?
            int alt97=2;
            int LA97_0 = input.LA(1);

            if ( (LA97_0==41) ) {
                alt97=1;
            }
            switch (alt97) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:666:50: ','
                    {
                    match(input,41,FOLLOW_41_in_elementValueArrayInitializer2994); if (failed) return ;

                    }
                    break;

            }

            match(input,45,FOLLOW_45_in_elementValueArrayInitializer2998); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 76, elementValueArrayInitializer_StartIndex); }
        }
        return ;
    }
    // $ANTLR end elementValueArrayInitializer


    // $ANTLR start annotationTypeDeclaration
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:669:1: annotationTypeDeclaration : '@' 'interface' Identifier annotationTypeBody ;
    public final void annotationTypeDeclaration() throws RecognitionException {
        int annotationTypeDeclaration_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 77) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:670:5: ( '@' 'interface' Identifier annotationTypeBody )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:670:9: '@' 'interface' Identifier annotationTypeBody
            {
            match(input,73,FOLLOW_73_in_annotationTypeDeclaration3021); if (failed) return ;
            match(input,46,FOLLOW_46_in_annotationTypeDeclaration3023); if (failed) return ;
            match(input,Identifier,FOLLOW_Identifier_in_annotationTypeDeclaration3025); if (failed) return ;
            pushFollow(FOLLOW_annotationTypeBody_in_annotationTypeDeclaration3027);
            annotationTypeBody();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 77, annotationTypeDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end annotationTypeDeclaration


    // $ANTLR start annotationTypeBody
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:673:1: annotationTypeBody : '{' ( annotationTypeElementDeclaration )* '}' ;
    public final void annotationTypeBody() throws RecognitionException {
        int annotationTypeBody_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 78) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:674:5: ( '{' ( annotationTypeElementDeclaration )* '}' )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:674:9: '{' ( annotationTypeElementDeclaration )* '}'
            {
            match(input,44,FOLLOW_44_in_annotationTypeBody3050); if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:674:13: ( annotationTypeElementDeclaration )*
            loop98:
            do {
                int alt98=2;
                int LA98_0 = input.LA(1);

                if ( ((LA98_0>=Identifier && LA98_0<=ENUM)||LA98_0==28||(LA98_0>=31 && LA98_0<=37)||LA98_0==46||(LA98_0>=52 && LA98_0<=63)||LA98_0==73) ) {
                    alt98=1;
                }


                switch (alt98) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:674:14: annotationTypeElementDeclaration
            	    {
            	    pushFollow(FOLLOW_annotationTypeElementDeclaration_in_annotationTypeBody3053);
            	    annotationTypeElementDeclaration();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop98;
                }
            } while (true);

            match(input,45,FOLLOW_45_in_annotationTypeBody3057); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 78, annotationTypeBody_StartIndex); }
        }
        return ;
    }
    // $ANTLR end annotationTypeBody


    // $ANTLR start annotationTypeElementDeclaration
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:677:1: annotationTypeElementDeclaration : modifiers annotationTypeElementRest ;
    public final void annotationTypeElementDeclaration() throws RecognitionException {
        int annotationTypeElementDeclaration_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 79) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:678:5: ( modifiers annotationTypeElementRest )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:678:9: modifiers annotationTypeElementRest
            {
            pushFollow(FOLLOW_modifiers_in_annotationTypeElementDeclaration3080);
            modifiers();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_annotationTypeElementRest_in_annotationTypeElementDeclaration3082);
            annotationTypeElementRest();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 79, annotationTypeElementDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end annotationTypeElementDeclaration


    // $ANTLR start annotationTypeElementRest
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:681:1: annotationTypeElementRest : ( type annotationMethodOrConstantRest ';' | normalClassDeclaration ( ';' )? | normalInterfaceDeclaration ( ';' )? | enumDeclaration ( ';' )? | annotationTypeDeclaration ( ';' )? );
    public final void annotationTypeElementRest() throws RecognitionException {
        int annotationTypeElementRest_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 80) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:682:5: ( type annotationMethodOrConstantRest ';' | normalClassDeclaration ( ';' )? | normalInterfaceDeclaration ( ';' )? | enumDeclaration ( ';' )? | annotationTypeDeclaration ( ';' )? )
            int alt103=5;
            switch ( input.LA(1) ) {
            case Identifier:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
                {
                alt103=1;
                }
                break;
            case 37:
                {
                alt103=2;
                }
                break;
            case 46:
                {
                alt103=3;
                }
                break;
            case ENUM:
                {
                alt103=4;
                }
                break;
            case 73:
                {
                alt103=5;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("681:1: annotationTypeElementRest : ( type annotationMethodOrConstantRest ';' | normalClassDeclaration ( ';' )? | normalInterfaceDeclaration ( ';' )? | enumDeclaration ( ';' )? | annotationTypeDeclaration ( ';' )? );", 103, 0, input);

                throw nvae;
            }

            switch (alt103) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:682:9: type annotationMethodOrConstantRest ';'
                    {
                    pushFollow(FOLLOW_type_in_annotationTypeElementRest3105);
                    type();
                    _fsp--;
                    if (failed) return ;
                    pushFollow(FOLLOW_annotationMethodOrConstantRest_in_annotationTypeElementRest3107);
                    annotationMethodOrConstantRest();
                    _fsp--;
                    if (failed) return ;
                    match(input,26,FOLLOW_26_in_annotationTypeElementRest3109); if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:683:9: normalClassDeclaration ( ';' )?
                    {
                    pushFollow(FOLLOW_normalClassDeclaration_in_annotationTypeElementRest3119);
                    normalClassDeclaration();
                    _fsp--;
                    if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:683:32: ( ';' )?
                    int alt99=2;
                    int LA99_0 = input.LA(1);

                    if ( (LA99_0==26) ) {
                        alt99=1;
                    }
                    switch (alt99) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: ';'
                            {
                            match(input,26,FOLLOW_26_in_annotationTypeElementRest3121); if (failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:684:9: normalInterfaceDeclaration ( ';' )?
                    {
                    pushFollow(FOLLOW_normalInterfaceDeclaration_in_annotationTypeElementRest3132);
                    normalInterfaceDeclaration();
                    _fsp--;
                    if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:684:36: ( ';' )?
                    int alt100=2;
                    int LA100_0 = input.LA(1);

                    if ( (LA100_0==26) ) {
                        alt100=1;
                    }
                    switch (alt100) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: ';'
                            {
                            match(input,26,FOLLOW_26_in_annotationTypeElementRest3134); if (failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 4 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:685:9: enumDeclaration ( ';' )?
                    {
                    pushFollow(FOLLOW_enumDeclaration_in_annotationTypeElementRest3145);
                    enumDeclaration();
                    _fsp--;
                    if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:685:25: ( ';' )?
                    int alt101=2;
                    int LA101_0 = input.LA(1);

                    if ( (LA101_0==26) ) {
                        alt101=1;
                    }
                    switch (alt101) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: ';'
                            {
                            match(input,26,FOLLOW_26_in_annotationTypeElementRest3147); if (failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 5 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:686:9: annotationTypeDeclaration ( ';' )?
                    {
                    pushFollow(FOLLOW_annotationTypeDeclaration_in_annotationTypeElementRest3158);
                    annotationTypeDeclaration();
                    _fsp--;
                    if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:686:35: ( ';' )?
                    int alt102=2;
                    int LA102_0 = input.LA(1);

                    if ( (LA102_0==26) ) {
                        alt102=1;
                    }
                    switch (alt102) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: ';'
                            {
                            match(input,26,FOLLOW_26_in_annotationTypeElementRest3160); if (failed) return ;

                            }
                            break;

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 80, annotationTypeElementRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end annotationTypeElementRest


    // $ANTLR start annotationMethodOrConstantRest
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:689:1: annotationMethodOrConstantRest : ( annotationMethodRest | annotationConstantRest );
    public final void annotationMethodOrConstantRest() throws RecognitionException {
        int annotationMethodOrConstantRest_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 81) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:690:5: ( annotationMethodRest | annotationConstantRest )
            int alt104=2;
            int LA104_0 = input.LA(1);

            if ( (LA104_0==Identifier) ) {
                int LA104_1 = input.LA(2);

                if ( (LA104_1==66) ) {
                    alt104=1;
                }
                else if ( (LA104_1==26||LA104_1==41||LA104_1==48||LA104_1==51) ) {
                    alt104=2;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("689:1: annotationMethodOrConstantRest : ( annotationMethodRest | annotationConstantRest );", 104, 1, input);

                    throw nvae;
                }
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("689:1: annotationMethodOrConstantRest : ( annotationMethodRest | annotationConstantRest );", 104, 0, input);

                throw nvae;
            }
            switch (alt104) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:690:9: annotationMethodRest
                    {
                    pushFollow(FOLLOW_annotationMethodRest_in_annotationMethodOrConstantRest3184);
                    annotationMethodRest();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:691:9: annotationConstantRest
                    {
                    pushFollow(FOLLOW_annotationConstantRest_in_annotationMethodOrConstantRest3194);
                    annotationConstantRest();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 81, annotationMethodOrConstantRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end annotationMethodOrConstantRest


    // $ANTLR start annotationMethodRest
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:694:1: annotationMethodRest : Identifier '(' ')' ( defaultValue )? ;
    public final void annotationMethodRest() throws RecognitionException {
        int annotationMethodRest_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 82) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:695:5: ( Identifier '(' ')' ( defaultValue )? )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:695:9: Identifier '(' ')' ( defaultValue )?
            {
            match(input,Identifier,FOLLOW_Identifier_in_annotationMethodRest3217); if (failed) return ;
            match(input,66,FOLLOW_66_in_annotationMethodRest3219); if (failed) return ;
            match(input,67,FOLLOW_67_in_annotationMethodRest3221); if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:695:28: ( defaultValue )?
            int alt105=2;
            int LA105_0 = input.LA(1);

            if ( (LA105_0==74) ) {
                alt105=1;
            }
            switch (alt105) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:695:29: defaultValue
                    {
                    pushFollow(FOLLOW_defaultValue_in_annotationMethodRest3224);
                    defaultValue();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 82, annotationMethodRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end annotationMethodRest


    // $ANTLR start annotationConstantRest
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:698:1: annotationConstantRest : variableDeclarators ;
    public final void annotationConstantRest() throws RecognitionException {
        int annotationConstantRest_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 83) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:699:5: ( variableDeclarators )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:699:9: variableDeclarators
            {
            pushFollow(FOLLOW_variableDeclarators_in_annotationConstantRest3249);
            variableDeclarators();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 83, annotationConstantRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end annotationConstantRest


    // $ANTLR start defaultValue
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:702:1: defaultValue : 'default' elementValue ;
    public final void defaultValue() throws RecognitionException {
        int defaultValue_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 84) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:703:5: ( 'default' elementValue )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:703:9: 'default' elementValue
            {
            match(input,74,FOLLOW_74_in_defaultValue3272); if (failed) return ;
            pushFollow(FOLLOW_elementValue_in_defaultValue3274);
            elementValue();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 84, defaultValue_StartIndex); }
        }
        return ;
    }
    // $ANTLR end defaultValue


    // $ANTLR start block
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:708:1: block : '{' ( blockStatement )* '}' ;
    public final void block() throws RecognitionException {
        int block_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 85) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:709:5: ( '{' ( blockStatement )* '}' )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:709:9: '{' ( blockStatement )* '}'
            {
            match(input,44,FOLLOW_44_in_block3295); if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:709:13: ( blockStatement )*
            loop106:
            do {
                int alt106=2;
                int LA106_0 = input.LA(1);

                if ( ((LA106_0>=Identifier && LA106_0<=ASSERT)||LA106_0==26||LA106_0==28||(LA106_0>=31 && LA106_0<=37)||LA106_0==44||(LA106_0>=46 && LA106_0<=47)||LA106_0==53||(LA106_0>=56 && LA106_0<=63)||(LA106_0>=65 && LA106_0<=66)||(LA106_0>=69 && LA106_0<=73)||LA106_0==76||(LA106_0>=78 && LA106_0<=81)||(LA106_0>=83 && LA106_0<=87)||(LA106_0>=105 && LA106_0<=106)||(LA106_0>=109 && LA106_0<=113)) ) {
                    alt106=1;
                }


                switch (alt106) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: blockStatement
            	    {
            	    pushFollow(FOLLOW_blockStatement_in_block3297);
            	    blockStatement();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop106;
                }
            } while (true);

            match(input,45,FOLLOW_45_in_block3300); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 85, block_StartIndex); }
        }
        return ;
    }
    // $ANTLR end block


    // $ANTLR start blockStatement
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:712:1: blockStatement : ( localVariableDeclarationStatement | classOrInterfaceDeclaration | statement );
    public final void blockStatement() throws RecognitionException {
        int blockStatement_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 86) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:713:5: ( localVariableDeclarationStatement | classOrInterfaceDeclaration | statement )
            int alt107=3;
            switch ( input.LA(1) ) {
            case 35:
                {
                switch ( input.LA(2) ) {
                case Identifier:
                case 56:
                case 57:
                case 58:
                case 59:
                case 60:
                case 61:
                case 62:
                case 63:
                    {
                    alt107=1;
                    }
                    break;
                case 35:
                    {
                    int LA107_47 = input.LA(3);

                    if ( (synpred151()) ) {
                        alt107=1;
                    }
                    else if ( (synpred152()) ) {
                        alt107=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("712:1: blockStatement : ( localVariableDeclarationStatement | classOrInterfaceDeclaration | statement );", 107, 47, input);

                        throw nvae;
                    }
                    }
                    break;
                case 73:
                    {
                    int LA107_48 = input.LA(3);

                    if ( (synpred151()) ) {
                        alt107=1;
                    }
                    else if ( (synpred152()) ) {
                        alt107=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("712:1: blockStatement : ( localVariableDeclarationStatement | classOrInterfaceDeclaration | statement );", 107, 48, input);

                        throw nvae;
                    }
                    }
                    break;
                case ENUM:
                case 28:
                case 31:
                case 32:
                case 33:
                case 34:
                case 36:
                case 37:
                case 46:
                    {
                    alt107=2;
                    }
                    break;
                default:
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("712:1: blockStatement : ( localVariableDeclarationStatement | classOrInterfaceDeclaration | statement );", 107, 1, input);

                    throw nvae;
                }

                }
                break;
            case 73:
                {
                int LA107_2 = input.LA(2);

                if ( (LA107_2==46) ) {
                    alt107=2;
                }
                else if ( (LA107_2==Identifier) ) {
                    int LA107_59 = input.LA(3);

                    if ( (synpred151()) ) {
                        alt107=1;
                    }
                    else if ( (synpred152()) ) {
                        alt107=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("712:1: blockStatement : ( localVariableDeclarationStatement | classOrInterfaceDeclaration | statement );", 107, 59, input);

                        throw nvae;
                    }
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("712:1: blockStatement : ( localVariableDeclarationStatement | classOrInterfaceDeclaration | statement );", 107, 2, input);

                    throw nvae;
                }
                }
                break;
            case Identifier:
                {
                switch ( input.LA(2) ) {
                case 26:
                case 30:
                case 42:
                case 43:
                case 51:
                case 64:
                case 66:
                case 75:
                case 90:
                case 91:
                case 92:
                case 93:
                case 94:
                case 95:
                case 96:
                case 97:
                case 98:
                case 99:
                case 100:
                case 101:
                case 102:
                case 103:
                case 104:
                case 105:
                case 106:
                case 107:
                case 108:
                case 109:
                case 110:
                    {
                    alt107=3;
                    }
                    break;
                case 29:
                    {
                    int LA107_61 = input.LA(3);

                    if ( (synpred151()) ) {
                        alt107=1;
                    }
                    else if ( (true) ) {
                        alt107=3;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("712:1: blockStatement : ( localVariableDeclarationStatement | classOrInterfaceDeclaration | statement );", 107, 61, input);

                        throw nvae;
                    }
                    }
                    break;
                case 48:
                    {
                    int LA107_62 = input.LA(3);

                    if ( (synpred151()) ) {
                        alt107=1;
                    }
                    else if ( (true) ) {
                        alt107=3;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("712:1: blockStatement : ( localVariableDeclarationStatement | classOrInterfaceDeclaration | statement );", 107, 62, input);

                        throw nvae;
                    }
                    }
                    break;
                case 40:
                    {
                    int LA107_67 = input.LA(3);

                    if ( (synpred151()) ) {
                        alt107=1;
                    }
                    else if ( (true) ) {
                        alt107=3;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("712:1: blockStatement : ( localVariableDeclarationStatement | classOrInterfaceDeclaration | statement );", 107, 67, input);

                        throw nvae;
                    }
                    }
                    break;
                case Identifier:
                    {
                    alt107=1;
                    }
                    break;
                default:
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("712:1: blockStatement : ( localVariableDeclarationStatement | classOrInterfaceDeclaration | statement );", 107, 3, input);

                    throw nvae;
                }

                }
                break;
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
                {
                switch ( input.LA(2) ) {
                case 48:
                    {
                    int LA107_88 = input.LA(3);

                    if ( (synpred151()) ) {
                        alt107=1;
                    }
                    else if ( (true) ) {
                        alt107=3;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("712:1: blockStatement : ( localVariableDeclarationStatement | classOrInterfaceDeclaration | statement );", 107, 88, input);

                        throw nvae;
                    }
                    }
                    break;
                case 29:
                    {
                    alt107=3;
                    }
                    break;
                case Identifier:
                    {
                    alt107=1;
                    }
                    break;
                default:
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("712:1: blockStatement : ( localVariableDeclarationStatement | classOrInterfaceDeclaration | statement );", 107, 4, input);

                    throw nvae;
                }

                }
                break;
            case ENUM:
            case 28:
            case 31:
            case 32:
            case 33:
            case 34:
            case 36:
            case 37:
            case 46:
                {
                alt107=2;
                }
                break;
            case FloatingPointLiteral:
            case CharacterLiteral:
            case StringLiteral:
            case HexLiteral:
            case OctalLiteral:
            case DecimalLiteral:
            case ASSERT:
            case 26:
            case 44:
            case 47:
            case 53:
            case 65:
            case 66:
            case 69:
            case 70:
            case 71:
            case 72:
            case 76:
            case 78:
            case 79:
            case 80:
            case 81:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 105:
            case 106:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
                {
                alt107=3;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("712:1: blockStatement : ( localVariableDeclarationStatement | classOrInterfaceDeclaration | statement );", 107, 0, input);

                throw nvae;
            }

            switch (alt107) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:713:9: localVariableDeclarationStatement
                    {
                    pushFollow(FOLLOW_localVariableDeclarationStatement_in_blockStatement3323);
                    localVariableDeclarationStatement();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:714:9: classOrInterfaceDeclaration
                    {
                    pushFollow(FOLLOW_classOrInterfaceDeclaration_in_blockStatement3333);
                    classOrInterfaceDeclaration();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 3 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:715:9: statement
                    {
                    pushFollow(FOLLOW_statement_in_blockStatement3343);
                    statement();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 86, blockStatement_StartIndex); }
        }
        return ;
    }
    // $ANTLR end blockStatement


    // $ANTLR start localVariableDeclarationStatement
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:718:1: localVariableDeclarationStatement : localVariableDeclaration ';' ;
    public final void localVariableDeclarationStatement() throws RecognitionException {
        int localVariableDeclarationStatement_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 87) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:719:5: ( localVariableDeclaration ';' )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:719:10: localVariableDeclaration ';'
            {
            pushFollow(FOLLOW_localVariableDeclaration_in_localVariableDeclarationStatement3367);
            localVariableDeclaration();
            _fsp--;
            if (failed) return ;
            match(input,26,FOLLOW_26_in_localVariableDeclarationStatement3369); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 87, localVariableDeclarationStatement_StartIndex); }
        }
        return ;
    }
    // $ANTLR end localVariableDeclarationStatement


    // $ANTLR start localVariableDeclaration
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:722:1: localVariableDeclaration : variableModifiers type variableDeclarators ;
    public final void localVariableDeclaration() throws RecognitionException {
        int localVariableDeclaration_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 88) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:723:5: ( variableModifiers type variableDeclarators )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:723:9: variableModifiers type variableDeclarators
            {
            pushFollow(FOLLOW_variableModifiers_in_localVariableDeclaration3388);
            variableModifiers();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_type_in_localVariableDeclaration3390);
            type();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_variableDeclarators_in_localVariableDeclaration3392);
            variableDeclarators();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 88, localVariableDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end localVariableDeclaration


    // $ANTLR start variableModifiers
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:726:1: variableModifiers : ( variableModifier )* ;
    public final void variableModifiers() throws RecognitionException {
        int variableModifiers_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 89) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:727:5: ( ( variableModifier )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:727:9: ( variableModifier )*
            {
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:727:9: ( variableModifier )*
            loop108:
            do {
                int alt108=2;
                int LA108_0 = input.LA(1);

                if ( (LA108_0==35||LA108_0==73) ) {
                    alt108=1;
                }


                switch (alt108) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: variableModifier
            	    {
            	    pushFollow(FOLLOW_variableModifier_in_variableModifiers3415);
            	    variableModifier();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop108;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 89, variableModifiers_StartIndex); }
        }
        return ;
    }
    // $ANTLR end variableModifiers


    // $ANTLR start statement
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:730:1: statement : ( block | ASSERT expression ( ':' expression )? ';' | 'if' parExpression statement ( options {k=1; } : 'else' statement )? | 'for' '(' forControl ')' statement | 'while' parExpression statement | 'do' statement 'while' parExpression ';' | 'try' block ( catches 'finally' block | catches | 'finally' block ) | 'switch' parExpression '{' switchBlockStatementGroups '}' | 'synchronized' parExpression block | 'return' ( expression )? ';' | 'throw' expression ';' | 'break' ( Identifier )? ';' | 'continue' ( Identifier )? ';' | ';' | statementExpression ';' | Identifier ':' statement );
    public final void statement() throws RecognitionException {
        int statement_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 90) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:731:5: ( block | ASSERT expression ( ':' expression )? ';' | 'if' parExpression statement ( options {k=1; } : 'else' statement )? | 'for' '(' forControl ')' statement | 'while' parExpression statement | 'do' statement 'while' parExpression ';' | 'try' block ( catches 'finally' block | catches | 'finally' block ) | 'switch' parExpression '{' switchBlockStatementGroups '}' | 'synchronized' parExpression block | 'return' ( expression )? ';' | 'throw' expression ';' | 'break' ( Identifier )? ';' | 'continue' ( Identifier )? ';' | ';' | statementExpression ';' | Identifier ':' statement )
            int alt115=16;
            switch ( input.LA(1) ) {
            case 44:
                {
                alt115=1;
                }
                break;
            case ASSERT:
                {
                alt115=2;
                }
                break;
            case 76:
                {
                alt115=3;
                }
                break;
            case 78:
                {
                alt115=4;
                }
                break;
            case 79:
                {
                alt115=5;
                }
                break;
            case 80:
                {
                alt115=6;
                }
                break;
            case 81:
                {
                alt115=7;
                }
                break;
            case 83:
                {
                alt115=8;
                }
                break;
            case 53:
                {
                alt115=9;
                }
                break;
            case 84:
                {
                alt115=10;
                }
                break;
            case 85:
                {
                alt115=11;
                }
                break;
            case 86:
                {
                alt115=12;
                }
                break;
            case 87:
                {
                alt115=13;
                }
                break;
            case 26:
                {
                alt115=14;
                }
                break;
            case FloatingPointLiteral:
            case CharacterLiteral:
            case StringLiteral:
            case HexLiteral:
            case OctalLiteral:
            case DecimalLiteral:
            case 47:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 65:
            case 66:
            case 69:
            case 70:
            case 71:
            case 72:
            case 105:
            case 106:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
                {
                alt115=15;
                }
                break;
            case Identifier:
                {
                int LA115_31 = input.LA(2);

                if ( (LA115_31==75) ) {
                    alt115=16;
                }
                else if ( (LA115_31==26||(LA115_31>=29 && LA115_31<=30)||LA115_31==40||(LA115_31>=42 && LA115_31<=43)||LA115_31==48||LA115_31==51||LA115_31==64||LA115_31==66||(LA115_31>=90 && LA115_31<=110)) ) {
                    alt115=15;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("730:1: statement : ( block | ASSERT expression ( ':' expression )? ';' | 'if' parExpression statement ( options {k=1; } : 'else' statement )? | 'for' '(' forControl ')' statement | 'while' parExpression statement | 'do' statement 'while' parExpression ';' | 'try' block ( catches 'finally' block | catches | 'finally' block ) | 'switch' parExpression '{' switchBlockStatementGroups '}' | 'synchronized' parExpression block | 'return' ( expression )? ';' | 'throw' expression ';' | 'break' ( Identifier )? ';' | 'continue' ( Identifier )? ';' | ';' | statementExpression ';' | Identifier ':' statement );", 115, 31, input);

                    throw nvae;
                }
                }
                break;
            default:
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("730:1: statement : ( block | ASSERT expression ( ':' expression )? ';' | 'if' parExpression statement ( options {k=1; } : 'else' statement )? | 'for' '(' forControl ')' statement | 'while' parExpression statement | 'do' statement 'while' parExpression ';' | 'try' block ( catches 'finally' block | catches | 'finally' block ) | 'switch' parExpression '{' switchBlockStatementGroups '}' | 'synchronized' parExpression block | 'return' ( expression )? ';' | 'throw' expression ';' | 'break' ( Identifier )? ';' | 'continue' ( Identifier )? ';' | ';' | statementExpression ';' | Identifier ':' statement );", 115, 0, input);

                throw nvae;
            }

            switch (alt115) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:731:7: block
                    {
                    pushFollow(FOLLOW_block_in_statement3433);
                    block();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:732:9: ASSERT expression ( ':' expression )? ';'
                    {
                    match(input,ASSERT,FOLLOW_ASSERT_in_statement3443); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_statement3445);
                    expression();
                    _fsp--;
                    if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:732:27: ( ':' expression )?
                    int alt109=2;
                    int LA109_0 = input.LA(1);

                    if ( (LA109_0==75) ) {
                        alt109=1;
                    }
                    switch (alt109) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:732:28: ':' expression
                            {
                            match(input,75,FOLLOW_75_in_statement3448); if (failed) return ;
                            pushFollow(FOLLOW_expression_in_statement3450);
                            expression();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }

                    match(input,26,FOLLOW_26_in_statement3454); if (failed) return ;

                    }
                    break;
                case 3 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:733:9: 'if' parExpression statement ( options {k=1; } : 'else' statement )?
                    {
                    match(input,76,FOLLOW_76_in_statement3464); if (failed) return ;
                    pushFollow(FOLLOW_parExpression_in_statement3466);
                    parExpression();
                    _fsp--;
                    if (failed) return ;
                    pushFollow(FOLLOW_statement_in_statement3468);
                    statement();
                    _fsp--;
                    if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:733:38: ( options {k=1; } : 'else' statement )?
                    int alt110=2;
                    int LA110_0 = input.LA(1);

                    if ( (LA110_0==77) ) {
                        int LA110_1 = input.LA(2);

                        if ( (synpred157()) ) {
                            alt110=1;
                        }
                    }
                    switch (alt110) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:733:54: 'else' statement
                            {
                            match(input,77,FOLLOW_77_in_statement3478); if (failed) return ;
                            pushFollow(FOLLOW_statement_in_statement3480);
                            statement();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 4 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:734:9: 'for' '(' forControl ')' statement
                    {
                    match(input,78,FOLLOW_78_in_statement3492); if (failed) return ;
                    match(input,66,FOLLOW_66_in_statement3494); if (failed) return ;
                    pushFollow(FOLLOW_forControl_in_statement3496);
                    forControl();
                    _fsp--;
                    if (failed) return ;
                    match(input,67,FOLLOW_67_in_statement3498); if (failed) return ;
                    pushFollow(FOLLOW_statement_in_statement3500);
                    statement();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 5 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:735:9: 'while' parExpression statement
                    {
                    match(input,79,FOLLOW_79_in_statement3510); if (failed) return ;
                    pushFollow(FOLLOW_parExpression_in_statement3512);
                    parExpression();
                    _fsp--;
                    if (failed) return ;
                    pushFollow(FOLLOW_statement_in_statement3514);
                    statement();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 6 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:736:9: 'do' statement 'while' parExpression ';'
                    {
                    match(input,80,FOLLOW_80_in_statement3524); if (failed) return ;
                    pushFollow(FOLLOW_statement_in_statement3526);
                    statement();
                    _fsp--;
                    if (failed) return ;
                    match(input,79,FOLLOW_79_in_statement3528); if (failed) return ;
                    pushFollow(FOLLOW_parExpression_in_statement3530);
                    parExpression();
                    _fsp--;
                    if (failed) return ;
                    match(input,26,FOLLOW_26_in_statement3532); if (failed) return ;

                    }
                    break;
                case 7 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:737:9: 'try' block ( catches 'finally' block | catches | 'finally' block )
                    {
                    match(input,81,FOLLOW_81_in_statement3542); if (failed) return ;
                    pushFollow(FOLLOW_block_in_statement3544);
                    block();
                    _fsp--;
                    if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:738:9: ( catches 'finally' block | catches | 'finally' block )
                    int alt111=3;
                    int LA111_0 = input.LA(1);

                    if ( (LA111_0==88) ) {
                        int LA111_1 = input.LA(2);

                        if ( (LA111_1==66) ) {
                            int LA111_3 = input.LA(3);

                            if ( (synpred162()) ) {
                                alt111=1;
                            }
                            else if ( (synpred163()) ) {
                                alt111=2;
                            }
                            else {
                                if (backtracking>0) {failed=true; return ;}
                                NoViableAltException nvae =
                                    new NoViableAltException("738:9: ( catches 'finally' block | catches | 'finally' block )", 111, 3, input);

                                throw nvae;
                            }
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("738:9: ( catches 'finally' block | catches | 'finally' block )", 111, 1, input);

                            throw nvae;
                        }
                    }
                    else if ( (LA111_0==82) ) {
                        alt111=3;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("738:9: ( catches 'finally' block | catches | 'finally' block )", 111, 0, input);

                        throw nvae;
                    }
                    switch (alt111) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:738:11: catches 'finally' block
                            {
                            pushFollow(FOLLOW_catches_in_statement3556);
                            catches();
                            _fsp--;
                            if (failed) return ;
                            match(input,82,FOLLOW_82_in_statement3558); if (failed) return ;
                            pushFollow(FOLLOW_block_in_statement3560);
                            block();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;
                        case 2 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:739:11: catches
                            {
                            pushFollow(FOLLOW_catches_in_statement3572);
                            catches();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;
                        case 3 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:740:13: 'finally' block
                            {
                            match(input,82,FOLLOW_82_in_statement3586); if (failed) return ;
                            pushFollow(FOLLOW_block_in_statement3588);
                            block();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 8 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:742:9: 'switch' parExpression '{' switchBlockStatementGroups '}'
                    {
                    match(input,83,FOLLOW_83_in_statement3608); if (failed) return ;
                    pushFollow(FOLLOW_parExpression_in_statement3610);
                    parExpression();
                    _fsp--;
                    if (failed) return ;
                    match(input,44,FOLLOW_44_in_statement3612); if (failed) return ;
                    pushFollow(FOLLOW_switchBlockStatementGroups_in_statement3614);
                    switchBlockStatementGroups();
                    _fsp--;
                    if (failed) return ;
                    match(input,45,FOLLOW_45_in_statement3616); if (failed) return ;

                    }
                    break;
                case 9 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:743:9: 'synchronized' parExpression block
                    {
                    match(input,53,FOLLOW_53_in_statement3626); if (failed) return ;
                    pushFollow(FOLLOW_parExpression_in_statement3628);
                    parExpression();
                    _fsp--;
                    if (failed) return ;
                    pushFollow(FOLLOW_block_in_statement3630);
                    block();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 10 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:744:9: 'return' ( expression )? ';'
                    {
                    match(input,84,FOLLOW_84_in_statement3640); if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:744:18: ( expression )?
                    int alt112=2;
                    int LA112_0 = input.LA(1);

                    if ( (LA112_0==Identifier||(LA112_0>=FloatingPointLiteral && LA112_0<=DecimalLiteral)||LA112_0==47||(LA112_0>=56 && LA112_0<=63)||(LA112_0>=65 && LA112_0<=66)||(LA112_0>=69 && LA112_0<=72)||(LA112_0>=105 && LA112_0<=106)||(LA112_0>=109 && LA112_0<=113)) ) {
                        alt112=1;
                    }
                    switch (alt112) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: expression
                            {
                            pushFollow(FOLLOW_expression_in_statement3642);
                            expression();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }

                    match(input,26,FOLLOW_26_in_statement3645); if (failed) return ;

                    }
                    break;
                case 11 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:745:9: 'throw' expression ';'
                    {
                    match(input,85,FOLLOW_85_in_statement3655); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_statement3657);
                    expression();
                    _fsp--;
                    if (failed) return ;
                    match(input,26,FOLLOW_26_in_statement3659); if (failed) return ;

                    }
                    break;
                case 12 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:746:9: 'break' ( Identifier )? ';'
                    {
                    match(input,86,FOLLOW_86_in_statement3669); if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:746:17: ( Identifier )?
                    int alt113=2;
                    int LA113_0 = input.LA(1);

                    if ( (LA113_0==Identifier) ) {
                        alt113=1;
                    }
                    switch (alt113) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: Identifier
                            {
                            match(input,Identifier,FOLLOW_Identifier_in_statement3671); if (failed) return ;

                            }
                            break;

                    }

                    match(input,26,FOLLOW_26_in_statement3674); if (failed) return ;

                    }
                    break;
                case 13 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:747:9: 'continue' ( Identifier )? ';'
                    {
                    match(input,87,FOLLOW_87_in_statement3684); if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:747:20: ( Identifier )?
                    int alt114=2;
                    int LA114_0 = input.LA(1);

                    if ( (LA114_0==Identifier) ) {
                        alt114=1;
                    }
                    switch (alt114) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: Identifier
                            {
                            match(input,Identifier,FOLLOW_Identifier_in_statement3686); if (failed) return ;

                            }
                            break;

                    }

                    match(input,26,FOLLOW_26_in_statement3689); if (failed) return ;

                    }
                    break;
                case 14 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:748:9: ';'
                    {
                    match(input,26,FOLLOW_26_in_statement3699); if (failed) return ;

                    }
                    break;
                case 15 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:749:9: statementExpression ';'
                    {
                    pushFollow(FOLLOW_statementExpression_in_statement3710);
                    statementExpression();
                    _fsp--;
                    if (failed) return ;
                    match(input,26,FOLLOW_26_in_statement3712); if (failed) return ;

                    }
                    break;
                case 16 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:750:9: Identifier ':' statement
                    {
                    match(input,Identifier,FOLLOW_Identifier_in_statement3722); if (failed) return ;
                    match(input,75,FOLLOW_75_in_statement3724); if (failed) return ;
                    pushFollow(FOLLOW_statement_in_statement3726);
                    statement();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 90, statement_StartIndex); }
        }
        return ;
    }
    // $ANTLR end statement


    // $ANTLR start catches
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:753:1: catches : catchClause ( catchClause )* ;
    public final void catches() throws RecognitionException {
        int catches_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 91) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:754:5: ( catchClause ( catchClause )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:754:9: catchClause ( catchClause )*
            {
            pushFollow(FOLLOW_catchClause_in_catches3749);
            catchClause();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:754:21: ( catchClause )*
            loop116:
            do {
                int alt116=2;
                int LA116_0 = input.LA(1);

                if ( (LA116_0==88) ) {
                    alt116=1;
                }


                switch (alt116) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:754:22: catchClause
            	    {
            	    pushFollow(FOLLOW_catchClause_in_catches3752);
            	    catchClause();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop116;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 91, catches_StartIndex); }
        }
        return ;
    }
    // $ANTLR end catches


    // $ANTLR start catchClause
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:757:1: catchClause : 'catch' '(' formalParameter ')' block ;
    public final void catchClause() throws RecognitionException {
        int catchClause_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 92) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:758:5: ( 'catch' '(' formalParameter ')' block )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:758:9: 'catch' '(' formalParameter ')' block
            {
            match(input,88,FOLLOW_88_in_catchClause3777); if (failed) return ;
            match(input,66,FOLLOW_66_in_catchClause3779); if (failed) return ;
            pushFollow(FOLLOW_formalParameter_in_catchClause3781);
            formalParameter();
            _fsp--;
            if (failed) return ;
            match(input,67,FOLLOW_67_in_catchClause3783); if (failed) return ;
            pushFollow(FOLLOW_block_in_catchClause3785);
            block();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 92, catchClause_StartIndex); }
        }
        return ;
    }
    // $ANTLR end catchClause


    // $ANTLR start formalParameter
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:761:1: formalParameter : variableModifiers type variableDeclaratorId ;
    public final void formalParameter() throws RecognitionException {
        int formalParameter_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 93) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:762:5: ( variableModifiers type variableDeclaratorId )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:762:9: variableModifiers type variableDeclaratorId
            {
            pushFollow(FOLLOW_variableModifiers_in_formalParameter3804);
            variableModifiers();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_type_in_formalParameter3806);
            type();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_variableDeclaratorId_in_formalParameter3808);
            variableDeclaratorId();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 93, formalParameter_StartIndex); }
        }
        return ;
    }
    // $ANTLR end formalParameter


    // $ANTLR start switchBlockStatementGroups
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:765:1: switchBlockStatementGroups : ( switchBlockStatementGroup )* ;
    public final void switchBlockStatementGroups() throws RecognitionException {
        int switchBlockStatementGroups_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 94) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:766:5: ( ( switchBlockStatementGroup )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:766:9: ( switchBlockStatementGroup )*
            {
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:766:9: ( switchBlockStatementGroup )*
            loop117:
            do {
                int alt117=2;
                int LA117_0 = input.LA(1);

                if ( (LA117_0==74||LA117_0==89) ) {
                    alt117=1;
                }


                switch (alt117) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:766:10: switchBlockStatementGroup
            	    {
            	    pushFollow(FOLLOW_switchBlockStatementGroup_in_switchBlockStatementGroups3836);
            	    switchBlockStatementGroup();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop117;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 94, switchBlockStatementGroups_StartIndex); }
        }
        return ;
    }
    // $ANTLR end switchBlockStatementGroups


    // $ANTLR start switchBlockStatementGroup
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:773:1: switchBlockStatementGroup : ( switchLabel )+ ( blockStatement )* ;
    public final void switchBlockStatementGroup() throws RecognitionException {
        int switchBlockStatementGroup_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 95) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:774:5: ( ( switchLabel )+ ( blockStatement )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:774:9: ( switchLabel )+ ( blockStatement )*
            {
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:774:9: ( switchLabel )+
            int cnt118=0;
            loop118:
            do {
                int alt118=2;
                int LA118_0 = input.LA(1);

                if ( (LA118_0==89) ) {
                    int LA118_46 = input.LA(2);

                    if ( (synpred178()) ) {
                        alt118=1;
                    }


                }
                else if ( (LA118_0==74) ) {
                    int LA118_47 = input.LA(2);

                    if ( (synpred178()) ) {
                        alt118=1;
                    }


                }


                switch (alt118) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: switchLabel
            	    {
            	    pushFollow(FOLLOW_switchLabel_in_switchBlockStatementGroup3863);
            	    switchLabel();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    if ( cnt118 >= 1 ) break loop118;
            	    if (backtracking>0) {failed=true; return ;}
                        EarlyExitException eee =
                            new EarlyExitException(118, input);
                        throw eee;
                }
                cnt118++;
            } while (true);

            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:774:22: ( blockStatement )*
            loop119:
            do {
                int alt119=2;
                int LA119_0 = input.LA(1);

                if ( ((LA119_0>=Identifier && LA119_0<=ASSERT)||LA119_0==26||LA119_0==28||(LA119_0>=31 && LA119_0<=37)||LA119_0==44||(LA119_0>=46 && LA119_0<=47)||LA119_0==53||(LA119_0>=56 && LA119_0<=63)||(LA119_0>=65 && LA119_0<=66)||(LA119_0>=69 && LA119_0<=73)||LA119_0==76||(LA119_0>=78 && LA119_0<=81)||(LA119_0>=83 && LA119_0<=87)||(LA119_0>=105 && LA119_0<=106)||(LA119_0>=109 && LA119_0<=113)) ) {
                    alt119=1;
                }


                switch (alt119) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: blockStatement
            	    {
            	    pushFollow(FOLLOW_blockStatement_in_switchBlockStatementGroup3866);
            	    blockStatement();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop119;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 95, switchBlockStatementGroup_StartIndex); }
        }
        return ;
    }
    // $ANTLR end switchBlockStatementGroup


    // $ANTLR start switchLabel
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:777:1: switchLabel : ( 'case' constantExpression ':' | 'case' enumConstantName ':' | 'default' ':' );
    public final void switchLabel() throws RecognitionException {
        int switchLabel_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 96) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:778:5: ( 'case' constantExpression ':' | 'case' enumConstantName ':' | 'default' ':' )
            int alt120=3;
            int LA120_0 = input.LA(1);

            if ( (LA120_0==89) ) {
                int LA120_1 = input.LA(2);

                if ( ((LA120_1>=FloatingPointLiteral && LA120_1<=DecimalLiteral)||LA120_1==47||(LA120_1>=56 && LA120_1<=63)||(LA120_1>=65 && LA120_1<=66)||(LA120_1>=69 && LA120_1<=72)||(LA120_1>=105 && LA120_1<=106)||(LA120_1>=109 && LA120_1<=113)) ) {
                    alt120=1;
                }
                else if ( (LA120_1==Identifier) ) {
                    int LA120_19 = input.LA(3);

                    if ( (synpred180()) ) {
                        alt120=1;
                    }
                    else if ( (synpred181()) ) {
                        alt120=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("777:1: switchLabel : ( 'case' constantExpression ':' | 'case' enumConstantName ':' | 'default' ':' );", 120, 19, input);

                        throw nvae;
                    }
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("777:1: switchLabel : ( 'case' constantExpression ':' | 'case' enumConstantName ':' | 'default' ':' );", 120, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA120_0==74) ) {
                alt120=3;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("777:1: switchLabel : ( 'case' constantExpression ':' | 'case' enumConstantName ':' | 'default' ':' );", 120, 0, input);

                throw nvae;
            }
            switch (alt120) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:778:9: 'case' constantExpression ':'
                    {
                    match(input,89,FOLLOW_89_in_switchLabel3890); if (failed) return ;
                    pushFollow(FOLLOW_constantExpression_in_switchLabel3892);
                    constantExpression();
                    _fsp--;
                    if (failed) return ;
                    match(input,75,FOLLOW_75_in_switchLabel3894); if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:779:9: 'case' enumConstantName ':'
                    {
                    match(input,89,FOLLOW_89_in_switchLabel3904); if (failed) return ;
                    pushFollow(FOLLOW_enumConstantName_in_switchLabel3906);
                    enumConstantName();
                    _fsp--;
                    if (failed) return ;
                    match(input,75,FOLLOW_75_in_switchLabel3908); if (failed) return ;

                    }
                    break;
                case 3 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:780:9: 'default' ':'
                    {
                    match(input,74,FOLLOW_74_in_switchLabel3918); if (failed) return ;
                    match(input,75,FOLLOW_75_in_switchLabel3920); if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 96, switchLabel_StartIndex); }
        }
        return ;
    }
    // $ANTLR end switchLabel


    // $ANTLR start forControl
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );
    public final void forControl() throws RecognitionException {
        int forControl_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 97) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:785:5: ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? )
            int alt124=2;
            switch ( input.LA(1) ) {
            case 35:
                {
                switch ( input.LA(2) ) {
                case Identifier:
                    {
                    switch ( input.LA(3) ) {
                    case 40:
                        {
                        int LA124_59 = input.LA(4);

                        if ( (synpred182()) ) {
                            alt124=1;
                        }
                        else if ( (true) ) {
                            alt124=2;
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 59, input);

                            throw nvae;
                        }
                        }
                        break;
                    case 29:
                        {
                        int LA124_60 = input.LA(4);

                        if ( (synpred182()) ) {
                            alt124=1;
                        }
                        else if ( (true) ) {
                            alt124=2;
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 60, input);

                            throw nvae;
                        }
                        }
                        break;
                    case 48:
                        {
                        int LA124_61 = input.LA(4);

                        if ( (synpred182()) ) {
                            alt124=1;
                        }
                        else if ( (true) ) {
                            alt124=2;
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 61, input);

                            throw nvae;
                        }
                        }
                        break;
                    case Identifier:
                        {
                        int LA124_62 = input.LA(4);

                        if ( (synpred182()) ) {
                            alt124=1;
                        }
                        else if ( (true) ) {
                            alt124=2;
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 62, input);

                            throw nvae;
                        }
                        }
                        break;
                    default:
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 23, input);

                        throw nvae;
                    }

                    }
                    break;
                case 56:
                case 57:
                case 58:
                case 59:
                case 60:
                case 61:
                case 62:
                case 63:
                    {
                    int LA124_24 = input.LA(3);

                    if ( (LA124_24==48) ) {
                        int LA124_63 = input.LA(4);

                        if ( (synpred182()) ) {
                            alt124=1;
                        }
                        else if ( (true) ) {
                            alt124=2;
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 63, input);

                            throw nvae;
                        }
                    }
                    else if ( (LA124_24==Identifier) ) {
                        int LA124_64 = input.LA(4);

                        if ( (synpred182()) ) {
                            alt124=1;
                        }
                        else if ( (true) ) {
                            alt124=2;
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 64, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 24, input);

                        throw nvae;
                    }
                    }
                    break;
                case 35:
                    {
                    switch ( input.LA(3) ) {
                    case Identifier:
                        {
                        int LA124_65 = input.LA(4);

                        if ( (synpred182()) ) {
                            alt124=1;
                        }
                        else if ( (true) ) {
                            alt124=2;
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 65, input);

                            throw nvae;
                        }
                        }
                        break;
                    case 56:
                    case 57:
                    case 58:
                    case 59:
                    case 60:
                    case 61:
                    case 62:
                    case 63:
                        {
                        int LA124_66 = input.LA(4);

                        if ( (synpred182()) ) {
                            alt124=1;
                        }
                        else if ( (true) ) {
                            alt124=2;
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 66, input);

                            throw nvae;
                        }
                        }
                        break;
                    case 35:
                        {
                        int LA124_67 = input.LA(4);

                        if ( (synpred182()) ) {
                            alt124=1;
                        }
                        else if ( (true) ) {
                            alt124=2;
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 67, input);

                            throw nvae;
                        }
                        }
                        break;
                    case 73:
                        {
                        int LA124_68 = input.LA(4);

                        if ( (synpred182()) ) {
                            alt124=1;
                        }
                        else if ( (true) ) {
                            alt124=2;
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 68, input);

                            throw nvae;
                        }
                        }
                        break;
                    default:
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 25, input);

                        throw nvae;
                    }

                    }
                    break;
                case 73:
                    {
                    int LA124_26 = input.LA(3);

                    if ( (LA124_26==Identifier) ) {
                        int LA124_69 = input.LA(4);

                        if ( (synpred182()) ) {
                            alt124=1;
                        }
                        else if ( (true) ) {
                            alt124=2;
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 69, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 26, input);

                        throw nvae;
                    }
                    }
                    break;
                default:
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 1, input);

                    throw nvae;
                }

                }
                break;
            case 73:
                {
                int LA124_2 = input.LA(2);

                if ( (LA124_2==Identifier) ) {
                    switch ( input.LA(3) ) {
                    case 29:
                        {
                        int LA124_70 = input.LA(4);

                        if ( (synpred182()) ) {
                            alt124=1;
                        }
                        else if ( (true) ) {
                            alt124=2;
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 70, input);

                            throw nvae;
                        }
                        }
                        break;
                    case 66:
                        {
                        int LA124_71 = input.LA(4);

                        if ( (synpred182()) ) {
                            alt124=1;
                        }
                        else if ( (true) ) {
                            alt124=2;
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 71, input);

                            throw nvae;
                        }
                        }
                        break;
                    case Identifier:
                        {
                        int LA124_72 = input.LA(4);

                        if ( (synpred182()) ) {
                            alt124=1;
                        }
                        else if ( (true) ) {
                            alt124=2;
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 72, input);

                            throw nvae;
                        }
                        }
                        break;
                    case 56:
                    case 57:
                    case 58:
                    case 59:
                    case 60:
                    case 61:
                    case 62:
                    case 63:
                        {
                        int LA124_73 = input.LA(4);

                        if ( (synpred182()) ) {
                            alt124=1;
                        }
                        else if ( (true) ) {
                            alt124=2;
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 73, input);

                            throw nvae;
                        }
                        }
                        break;
                    case 35:
                        {
                        int LA124_74 = input.LA(4);

                        if ( (synpred182()) ) {
                            alt124=1;
                        }
                        else if ( (true) ) {
                            alt124=2;
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 74, input);

                            throw nvae;
                        }
                        }
                        break;
                    case 73:
                        {
                        int LA124_75 = input.LA(4);

                        if ( (synpred182()) ) {
                            alt124=1;
                        }
                        else if ( (true) ) {
                            alt124=2;
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 75, input);

                            throw nvae;
                        }
                        }
                        break;
                    default:
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 27, input);

                        throw nvae;
                    }

                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 2, input);

                    throw nvae;
                }
                }
                break;
            case Identifier:
                {
                switch ( input.LA(2) ) {
                case 29:
                    {
                    int LA124_28 = input.LA(3);

                    if ( (LA124_28==37||LA124_28==40||LA124_28==65||LA124_28==69||LA124_28==113) ) {
                        alt124=2;
                    }
                    else if ( (LA124_28==Identifier) ) {
                        int LA124_79 = input.LA(4);

                        if ( (synpred182()) ) {
                            alt124=1;
                        }
                        else if ( (true) ) {
                            alt124=2;
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 79, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 28, input);

                        throw nvae;
                    }
                    }
                    break;
                case 48:
                    {
                    int LA124_29 = input.LA(3);

                    if ( (LA124_29==49) ) {
                        int LA124_82 = input.LA(4);

                        if ( (synpred182()) ) {
                            alt124=1;
                        }
                        else if ( (true) ) {
                            alt124=2;
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 82, input);

                            throw nvae;
                        }
                    }
                    else if ( (LA124_29==Identifier||(LA124_29>=FloatingPointLiteral && LA124_29<=DecimalLiteral)||LA124_29==47||(LA124_29>=56 && LA124_29<=63)||(LA124_29>=65 && LA124_29<=66)||(LA124_29>=69 && LA124_29<=72)||(LA124_29>=105 && LA124_29<=106)||(LA124_29>=109 && LA124_29<=113)) ) {
                        alt124=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 29, input);

                        throw nvae;
                    }
                    }
                    break;
                case 26:
                case 30:
                case 41:
                case 42:
                case 43:
                case 51:
                case 64:
                case 66:
                case 90:
                case 91:
                case 92:
                case 93:
                case 94:
                case 95:
                case 96:
                case 97:
                case 98:
                case 99:
                case 100:
                case 101:
                case 102:
                case 103:
                case 104:
                case 105:
                case 106:
                case 107:
                case 108:
                case 109:
                case 110:
                    {
                    alt124=2;
                    }
                    break;
                case 40:
                    {
                    switch ( input.LA(3) ) {
                    case FloatingPointLiteral:
                    case CharacterLiteral:
                    case StringLiteral:
                    case HexLiteral:
                    case OctalLiteral:
                    case DecimalLiteral:
                    case 40:
                    case 47:
                    case 51:
                    case 65:
                    case 66:
                    case 69:
                    case 70:
                    case 71:
                    case 72:
                    case 105:
                    case 106:
                    case 109:
                    case 110:
                    case 111:
                    case 112:
                    case 113:
                        {
                        alt124=2;
                        }
                        break;
                    case Identifier:
                        {
                        int LA124_104 = input.LA(4);

                        if ( (synpred182()) ) {
                            alt124=1;
                        }
                        else if ( (true) ) {
                            alt124=2;
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 104, input);

                            throw nvae;
                        }
                        }
                        break;
                    case 56:
                    case 57:
                    case 58:
                    case 59:
                    case 60:
                    case 61:
                    case 62:
                    case 63:
                        {
                        int LA124_105 = input.LA(4);

                        if ( (synpred182()) ) {
                            alt124=1;
                        }
                        else if ( (true) ) {
                            alt124=2;
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 105, input);

                            throw nvae;
                        }
                        }
                        break;
                    case 64:
                        {
                        int LA124_106 = input.LA(4);

                        if ( (synpred182()) ) {
                            alt124=1;
                        }
                        else if ( (true) ) {
                            alt124=2;
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 106, input);

                            throw nvae;
                        }
                        }
                        break;
                    default:
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 34, input);

                        throw nvae;
                    }

                    }
                    break;
                case Identifier:
                    {
                    int LA124_55 = input.LA(3);

                    if ( (LA124_55==75) ) {
                        alt124=1;
                    }
                    else if ( (LA124_55==26||LA124_55==41||LA124_55==48||LA124_55==51) ) {
                        alt124=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 55, input);

                        throw nvae;
                    }
                    }
                    break;
                default:
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 3, input);

                    throw nvae;
                }

                }
                break;
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
                {
                switch ( input.LA(2) ) {
                case 48:
                    {
                    int LA124_56 = input.LA(3);

                    if ( (LA124_56==49) ) {
                        int LA124_129 = input.LA(4);

                        if ( (synpred182()) ) {
                            alt124=1;
                        }
                        else if ( (true) ) {
                            alt124=2;
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 129, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 56, input);

                        throw nvae;
                    }
                    }
                    break;
                case Identifier:
                    {
                    int LA124_57 = input.LA(3);

                    if ( (LA124_57==75) ) {
                        alt124=1;
                    }
                    else if ( (LA124_57==26||LA124_57==41||LA124_57==48||LA124_57==51) ) {
                        alt124=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 57, input);

                        throw nvae;
                    }
                    }
                    break;
                case 29:
                    {
                    alt124=2;
                    }
                    break;
                default:
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 4, input);

                    throw nvae;
                }

                }
                break;
            case FloatingPointLiteral:
            case CharacterLiteral:
            case StringLiteral:
            case HexLiteral:
            case OctalLiteral:
            case DecimalLiteral:
            case 26:
            case 47:
            case 65:
            case 66:
            case 69:
            case 70:
            case 71:
            case 72:
            case 105:
            case 106:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
                {
                alt124=2;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("783:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );", 124, 0, input);

                throw nvae;
            }

            switch (alt124) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:785:9: enhancedForControl
                    {
                    pushFollow(FOLLOW_enhancedForControl_in_forControl3951);
                    enhancedForControl();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:786:9: ( forInit )? ';' ( expression )? ';' ( forUpdate )?
                    {
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:786:9: ( forInit )?
                    int alt121=2;
                    int LA121_0 = input.LA(1);

                    if ( (LA121_0==Identifier||(LA121_0>=FloatingPointLiteral && LA121_0<=DecimalLiteral)||LA121_0==35||LA121_0==47||(LA121_0>=56 && LA121_0<=63)||(LA121_0>=65 && LA121_0<=66)||(LA121_0>=69 && LA121_0<=73)||(LA121_0>=105 && LA121_0<=106)||(LA121_0>=109 && LA121_0<=113)) ) {
                        alt121=1;
                    }
                    switch (alt121) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: forInit
                            {
                            pushFollow(FOLLOW_forInit_in_forControl3961);
                            forInit();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }

                    match(input,26,FOLLOW_26_in_forControl3964); if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:786:22: ( expression )?
                    int alt122=2;
                    int LA122_0 = input.LA(1);

                    if ( (LA122_0==Identifier||(LA122_0>=FloatingPointLiteral && LA122_0<=DecimalLiteral)||LA122_0==47||(LA122_0>=56 && LA122_0<=63)||(LA122_0>=65 && LA122_0<=66)||(LA122_0>=69 && LA122_0<=72)||(LA122_0>=105 && LA122_0<=106)||(LA122_0>=109 && LA122_0<=113)) ) {
                        alt122=1;
                    }
                    switch (alt122) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: expression
                            {
                            pushFollow(FOLLOW_expression_in_forControl3966);
                            expression();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }

                    match(input,26,FOLLOW_26_in_forControl3969); if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:786:38: ( forUpdate )?
                    int alt123=2;
                    int LA123_0 = input.LA(1);

                    if ( (LA123_0==Identifier||(LA123_0>=FloatingPointLiteral && LA123_0<=DecimalLiteral)||LA123_0==47||(LA123_0>=56 && LA123_0<=63)||(LA123_0>=65 && LA123_0<=66)||(LA123_0>=69 && LA123_0<=72)||(LA123_0>=105 && LA123_0<=106)||(LA123_0>=109 && LA123_0<=113)) ) {
                        alt123=1;
                    }
                    switch (alt123) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: forUpdate
                            {
                            pushFollow(FOLLOW_forUpdate_in_forControl3971);
                            forUpdate();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 97, forControl_StartIndex); }
        }
        return ;
    }
    // $ANTLR end forControl


    // $ANTLR start forInit
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:789:1: forInit : ( localVariableDeclaration | expressionList );
    public final void forInit() throws RecognitionException {
        int forInit_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 98) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:790:5: ( localVariableDeclaration | expressionList )
            int alt125=2;
            switch ( input.LA(1) ) {
            case 35:
            case 73:
                {
                alt125=1;
                }
                break;
            case Identifier:
                {
                switch ( input.LA(2) ) {
                case 40:
                    {
                    int LA125_22 = input.LA(3);

                    if ( (synpred186()) ) {
                        alt125=1;
                    }
                    else if ( (true) ) {
                        alt125=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("789:1: forInit : ( localVariableDeclaration | expressionList );", 125, 22, input);

                        throw nvae;
                    }
                    }
                    break;
                case 29:
                    {
                    int LA125_23 = input.LA(3);

                    if ( (synpred186()) ) {
                        alt125=1;
                    }
                    else if ( (true) ) {
                        alt125=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("789:1: forInit : ( localVariableDeclaration | expressionList );", 125, 23, input);

                        throw nvae;
                    }
                    }
                    break;
                case 48:
                    {
                    int LA125_24 = input.LA(3);

                    if ( (synpred186()) ) {
                        alt125=1;
                    }
                    else if ( (true) ) {
                        alt125=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("789:1: forInit : ( localVariableDeclaration | expressionList );", 125, 24, input);

                        throw nvae;
                    }
                    }
                    break;
                case Identifier:
                    {
                    alt125=1;
                    }
                    break;
                case EOF:
                case 26:
                case 30:
                case 41:
                case 42:
                case 43:
                case 51:
                case 64:
                case 66:
                case 90:
                case 91:
                case 92:
                case 93:
                case 94:
                case 95:
                case 96:
                case 97:
                case 98:
                case 99:
                case 100:
                case 101:
                case 102:
                case 103:
                case 104:
                case 105:
                case 106:
                case 107:
                case 108:
                case 109:
                case 110:
                    {
                    alt125=2;
                    }
                    break;
                default:
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("789:1: forInit : ( localVariableDeclaration | expressionList );", 125, 3, input);

                    throw nvae;
                }

                }
                break;
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
                {
                switch ( input.LA(2) ) {
                case 48:
                    {
                    int LA125_51 = input.LA(3);

                    if ( (synpred186()) ) {
                        alt125=1;
                    }
                    else if ( (true) ) {
                        alt125=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("789:1: forInit : ( localVariableDeclaration | expressionList );", 125, 51, input);

                        throw nvae;
                    }
                    }
                    break;
                case Identifier:
                    {
                    alt125=1;
                    }
                    break;
                case 29:
                    {
                    alt125=2;
                    }
                    break;
                default:
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("789:1: forInit : ( localVariableDeclaration | expressionList );", 125, 4, input);

                    throw nvae;
                }

                }
                break;
            case FloatingPointLiteral:
            case CharacterLiteral:
            case StringLiteral:
            case HexLiteral:
            case OctalLiteral:
            case DecimalLiteral:
            case 47:
            case 65:
            case 66:
            case 69:
            case 70:
            case 71:
            case 72:
            case 105:
            case 106:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
                {
                alt125=2;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("789:1: forInit : ( localVariableDeclaration | expressionList );", 125, 0, input);

                throw nvae;
            }

            switch (alt125) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:790:9: localVariableDeclaration
                    {
                    pushFollow(FOLLOW_localVariableDeclaration_in_forInit3991);
                    localVariableDeclaration();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:791:9: expressionList
                    {
                    pushFollow(FOLLOW_expressionList_in_forInit4001);
                    expressionList();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 98, forInit_StartIndex); }
        }
        return ;
    }
    // $ANTLR end forInit


    // $ANTLR start enhancedForControl
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:794:1: enhancedForControl : variableModifiers type Identifier ':' expression ;
    public final void enhancedForControl() throws RecognitionException {
        int enhancedForControl_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 99) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:795:5: ( variableModifiers type Identifier ':' expression )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:795:9: variableModifiers type Identifier ':' expression
            {
            pushFollow(FOLLOW_variableModifiers_in_enhancedForControl4024);
            variableModifiers();
            _fsp--;
            if (failed) return ;
            pushFollow(FOLLOW_type_in_enhancedForControl4026);
            type();
            _fsp--;
            if (failed) return ;
            match(input,Identifier,FOLLOW_Identifier_in_enhancedForControl4028); if (failed) return ;
            match(input,75,FOLLOW_75_in_enhancedForControl4030); if (failed) return ;
            pushFollow(FOLLOW_expression_in_enhancedForControl4032);
            expression();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 99, enhancedForControl_StartIndex); }
        }
        return ;
    }
    // $ANTLR end enhancedForControl


    // $ANTLR start forUpdate
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:798:1: forUpdate : expressionList ;
    public final void forUpdate() throws RecognitionException {
        int forUpdate_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 100) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:799:5: ( expressionList )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:799:9: expressionList
            {
            pushFollow(FOLLOW_expressionList_in_forUpdate4051);
            expressionList();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 100, forUpdate_StartIndex); }
        }
        return ;
    }
    // $ANTLR end forUpdate


    // $ANTLR start parExpression
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:804:1: parExpression : '(' expression ')' ;
    public final void parExpression() throws RecognitionException {
        int parExpression_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 101) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:805:5: ( '(' expression ')' )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:805:9: '(' expression ')'
            {
            match(input,66,FOLLOW_66_in_parExpression4072); if (failed) return ;
            pushFollow(FOLLOW_expression_in_parExpression4074);
            expression();
            _fsp--;
            if (failed) return ;
            match(input,67,FOLLOW_67_in_parExpression4076); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 101, parExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end parExpression


    // $ANTLR start expressionList
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:808:1: expressionList : expression ( ',' expression )* ;
    public final void expressionList() throws RecognitionException {
        int expressionList_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 102) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:809:5: ( expression ( ',' expression )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:809:9: expression ( ',' expression )*
            {
            pushFollow(FOLLOW_expression_in_expressionList4099);
            expression();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:809:20: ( ',' expression )*
            loop126:
            do {
                int alt126=2;
                int LA126_0 = input.LA(1);

                if ( (LA126_0==41) ) {
                    alt126=1;
                }


                switch (alt126) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:809:21: ',' expression
            	    {
            	    match(input,41,FOLLOW_41_in_expressionList4102); if (failed) return ;
            	    pushFollow(FOLLOW_expression_in_expressionList4104);
            	    expression();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop126;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 102, expressionList_StartIndex); }
        }
        return ;
    }
    // $ANTLR end expressionList


    // $ANTLR start statementExpression
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:812:1: statementExpression : expression ;
    public final void statementExpression() throws RecognitionException {
        int statementExpression_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 103) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:813:5: ( expression )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:813:9: expression
            {
            pushFollow(FOLLOW_expression_in_statementExpression4125);
            expression();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 103, statementExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end statementExpression


    // $ANTLR start constantExpression
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:816:1: constantExpression : expression ;
    public final void constantExpression() throws RecognitionException {
        int constantExpression_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 104) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:817:5: ( expression )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:817:9: expression
            {
            pushFollow(FOLLOW_expression_in_constantExpression4148);
            expression();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 104, constantExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end constantExpression


    // $ANTLR start expression
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:820:1: expression : conditionalExpression ( assignmentOperator expression )? ;
    public final void expression() throws RecognitionException {
        int expression_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 105) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:821:5: ( conditionalExpression ( assignmentOperator expression )? )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:821:9: conditionalExpression ( assignmentOperator expression )?
            {
            pushFollow(FOLLOW_conditionalExpression_in_expression4171);
            conditionalExpression();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:821:31: ( assignmentOperator expression )?
            int alt127=2;
            switch ( input.LA(1) ) {
                case 51:
                    {
                    int LA127_1 = input.LA(2);

                    if ( (synpred188()) ) {
                        alt127=1;
                    }
                    }
                    break;
                case 90:
                    {
                    int LA127_2 = input.LA(2);

                    if ( (synpred188()) ) {
                        alt127=1;
                    }
                    }
                    break;
                case 91:
                    {
                    int LA127_3 = input.LA(2);

                    if ( (synpred188()) ) {
                        alt127=1;
                    }
                    }
                    break;
                case 92:
                    {
                    int LA127_4 = input.LA(2);

                    if ( (synpred188()) ) {
                        alt127=1;
                    }
                    }
                    break;
                case 93:
                    {
                    int LA127_5 = input.LA(2);

                    if ( (synpred188()) ) {
                        alt127=1;
                    }
                    }
                    break;
                case 94:
                    {
                    int LA127_6 = input.LA(2);

                    if ( (synpred188()) ) {
                        alt127=1;
                    }
                    }
                    break;
                case 95:
                    {
                    int LA127_7 = input.LA(2);

                    if ( (synpred188()) ) {
                        alt127=1;
                    }
                    }
                    break;
                case 96:
                    {
                    int LA127_8 = input.LA(2);

                    if ( (synpred188()) ) {
                        alt127=1;
                    }
                    }
                    break;
                case 97:
                    {
                    int LA127_9 = input.LA(2);

                    if ( (synpred188()) ) {
                        alt127=1;
                    }
                    }
                    break;
                case 40:
                    {
                    int LA127_10 = input.LA(2);

                    if ( (synpred188()) ) {
                        alt127=1;
                    }
                    }
                    break;
                case 42:
                    {
                    int LA127_11 = input.LA(2);

                    if ( (synpred188()) ) {
                        alt127=1;
                    }
                    }
                    break;
            }

            switch (alt127) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:821:32: assignmentOperator expression
                    {
                    pushFollow(FOLLOW_assignmentOperator_in_expression4174);
                    assignmentOperator();
                    _fsp--;
                    if (failed) return ;
                    pushFollow(FOLLOW_expression_in_expression4176);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 105, expression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end expression


    // $ANTLR start assignmentOperator
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:824:1: assignmentOperator : ( '=' | '+=' | '-=' | '*=' | '/=' | '&=' | '|=' | '^=' | '%=' | ( '<' '<' '=' )=>t1= '<' t2= '<' t3= '=' {...}? | ( '>' '>' '>' '=' )=>t1= '>' t2= '>' t3= '>' t4= '=' {...}? | ( '>' '>' '=' )=>t1= '>' t2= '>' t3= '=' {...}?);
    public final void assignmentOperator() throws RecognitionException {
        int assignmentOperator_StartIndex = input.index();
        Token t1=null;
        Token t2=null;
        Token t3=null;
        Token t4=null;

        try {
            if ( backtracking>0 && alreadyParsedRule(input, 106) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:825:5: ( '=' | '+=' | '-=' | '*=' | '/=' | '&=' | '|=' | '^=' | '%=' | ( '<' '<' '=' )=>t1= '<' t2= '<' t3= '=' {...}? | ( '>' '>' '>' '=' )=>t1= '>' t2= '>' t3= '>' t4= '=' {...}? | ( '>' '>' '=' )=>t1= '>' t2= '>' t3= '=' {...}?)
            int alt128=12;
            int LA128_0 = input.LA(1);

            if ( (LA128_0==51) ) {
                alt128=1;
            }
            else if ( (LA128_0==90) ) {
                alt128=2;
            }
            else if ( (LA128_0==91) ) {
                alt128=3;
            }
            else if ( (LA128_0==92) ) {
                alt128=4;
            }
            else if ( (LA128_0==93) ) {
                alt128=5;
            }
            else if ( (LA128_0==94) ) {
                alt128=6;
            }
            else if ( (LA128_0==95) ) {
                alt128=7;
            }
            else if ( (LA128_0==96) ) {
                alt128=8;
            }
            else if ( (LA128_0==97) ) {
                alt128=9;
            }
            else if ( (LA128_0==40) && (synpred198())) {
                alt128=10;
            }
            else if ( (LA128_0==42) ) {
                int LA128_11 = input.LA(2);

                if ( (LA128_11==42) ) {
                    int LA128_12 = input.LA(3);

                    if ( (synpred199()) ) {
                        alt128=11;
                    }
                    else if ( (synpred200()) ) {
                        alt128=12;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("824:1: assignmentOperator : ( '=' | '+=' | '-=' | '*=' | '/=' | '&=' | '|=' | '^=' | '%=' | ( '<' '<' '=' )=>t1= '<' t2= '<' t3= '=' {...}? | ( '>' '>' '>' '=' )=>t1= '>' t2= '>' t3= '>' t4= '=' {...}? | ( '>' '>' '=' )=>t1= '>' t2= '>' t3= '=' {...}?);", 128, 12, input);

                        throw nvae;
                    }
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("824:1: assignmentOperator : ( '=' | '+=' | '-=' | '*=' | '/=' | '&=' | '|=' | '^=' | '%=' | ( '<' '<' '=' )=>t1= '<' t2= '<' t3= '=' {...}? | ( '>' '>' '>' '=' )=>t1= '>' t2= '>' t3= '>' t4= '=' {...}? | ( '>' '>' '=' )=>t1= '>' t2= '>' t3= '=' {...}?);", 128, 11, input);

                    throw nvae;
                }
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("824:1: assignmentOperator : ( '=' | '+=' | '-=' | '*=' | '/=' | '&=' | '|=' | '^=' | '%=' | ( '<' '<' '=' )=>t1= '<' t2= '<' t3= '=' {...}? | ( '>' '>' '>' '=' )=>t1= '>' t2= '>' t3= '>' t4= '=' {...}? | ( '>' '>' '=' )=>t1= '>' t2= '>' t3= '=' {...}?);", 128, 0, input);

                throw nvae;
            }
            switch (alt128) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:825:9: '='
                    {
                    match(input,51,FOLLOW_51_in_assignmentOperator4201); if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:826:9: '+='
                    {
                    match(input,90,FOLLOW_90_in_assignmentOperator4211); if (failed) return ;

                    }
                    break;
                case 3 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:827:9: '-='
                    {
                    match(input,91,FOLLOW_91_in_assignmentOperator4221); if (failed) return ;

                    }
                    break;
                case 4 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:828:9: '*='
                    {
                    match(input,92,FOLLOW_92_in_assignmentOperator4231); if (failed) return ;

                    }
                    break;
                case 5 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:829:9: '/='
                    {
                    match(input,93,FOLLOW_93_in_assignmentOperator4241); if (failed) return ;

                    }
                    break;
                case 6 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:830:9: '&='
                    {
                    match(input,94,FOLLOW_94_in_assignmentOperator4251); if (failed) return ;

                    }
                    break;
                case 7 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:831:9: '|='
                    {
                    match(input,95,FOLLOW_95_in_assignmentOperator4261); if (failed) return ;

                    }
                    break;
                case 8 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:832:9: '^='
                    {
                    match(input,96,FOLLOW_96_in_assignmentOperator4271); if (failed) return ;

                    }
                    break;
                case 9 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:833:9: '%='
                    {
                    match(input,97,FOLLOW_97_in_assignmentOperator4281); if (failed) return ;

                    }
                    break;
                case 10 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:834:9: ( '<' '<' '=' )=>t1= '<' t2= '<' t3= '=' {...}?
                    {
                    t1=(Token)input.LT(1);
                    match(input,40,FOLLOW_40_in_assignmentOperator4302); if (failed) return ;
                    t2=(Token)input.LT(1);
                    match(input,40,FOLLOW_40_in_assignmentOperator4306); if (failed) return ;
                    t3=(Token)input.LT(1);
                    match(input,51,FOLLOW_51_in_assignmentOperator4310); if (failed) return ;
                    if ( !( t1.getLine() == t2.getLine() &&
                              t1.getCharPositionInLine() + 1 == t2.getCharPositionInLine() && 
                              t2.getLine() == t3.getLine() && 
                              t2.getCharPositionInLine() + 1 == t3.getCharPositionInLine() ) ) {
                        if (backtracking>0) {failed=true; return ;}
                        throw new FailedPredicateException(input, "assignmentOperator", " $t1.getLine() == $t2.getLine() &&\n          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() && \n          $t2.getLine() == $t3.getLine() && \n          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() ");
                    }

                    }
                    break;
                case 11 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:839:9: ( '>' '>' '>' '=' )=>t1= '>' t2= '>' t3= '>' t4= '=' {...}?
                    {
                    t1=(Token)input.LT(1);
                    match(input,42,FOLLOW_42_in_assignmentOperator4344); if (failed) return ;
                    t2=(Token)input.LT(1);
                    match(input,42,FOLLOW_42_in_assignmentOperator4348); if (failed) return ;
                    t3=(Token)input.LT(1);
                    match(input,42,FOLLOW_42_in_assignmentOperator4352); if (failed) return ;
                    t4=(Token)input.LT(1);
                    match(input,51,FOLLOW_51_in_assignmentOperator4356); if (failed) return ;
                    if ( !( t1.getLine() == t2.getLine() && 
                              t1.getCharPositionInLine() + 1 == t2.getCharPositionInLine() &&
                              t2.getLine() == t3.getLine() && 
                              t2.getCharPositionInLine() + 1 == t3.getCharPositionInLine() &&
                              t3.getLine() == t4.getLine() && 
                              t3.getCharPositionInLine() + 1 == t4.getCharPositionInLine() ) ) {
                        if (backtracking>0) {failed=true; return ;}
                        throw new FailedPredicateException(input, "assignmentOperator", " $t1.getLine() == $t2.getLine() && \n          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() &&\n          $t2.getLine() == $t3.getLine() && \n          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() &&\n          $t3.getLine() == $t4.getLine() && \n          $t3.getCharPositionInLine() + 1 == $t4.getCharPositionInLine() ");
                    }

                    }
                    break;
                case 12 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:846:9: ( '>' '>' '=' )=>t1= '>' t2= '>' t3= '=' {...}?
                    {
                    t1=(Token)input.LT(1);
                    match(input,42,FOLLOW_42_in_assignmentOperator4387); if (failed) return ;
                    t2=(Token)input.LT(1);
                    match(input,42,FOLLOW_42_in_assignmentOperator4391); if (failed) return ;
                    t3=(Token)input.LT(1);
                    match(input,51,FOLLOW_51_in_assignmentOperator4395); if (failed) return ;
                    if ( !( t1.getLine() == t2.getLine() && 
                              t1.getCharPositionInLine() + 1 == t2.getCharPositionInLine() && 
                              t2.getLine() == t3.getLine() && 
                              t2.getCharPositionInLine() + 1 == t3.getCharPositionInLine() ) ) {
                        if (backtracking>0) {failed=true; return ;}
                        throw new FailedPredicateException(input, "assignmentOperator", " $t1.getLine() == $t2.getLine() && \n          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() && \n          $t2.getLine() == $t3.getLine() && \n          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() ");
                    }

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 106, assignmentOperator_StartIndex); }
        }
        return ;
    }
    // $ANTLR end assignmentOperator


    // $ANTLR start conditionalExpression
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:853:1: conditionalExpression : conditionalOrExpression ( '?' expression ':' expression )? ;
    public final void conditionalExpression() throws RecognitionException {
        int conditionalExpression_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 107) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:854:5: ( conditionalOrExpression ( '?' expression ':' expression )? )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:854:9: conditionalOrExpression ( '?' expression ':' expression )?
            {
            pushFollow(FOLLOW_conditionalOrExpression_in_conditionalExpression4424);
            conditionalOrExpression();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:854:33: ( '?' expression ':' expression )?
            int alt129=2;
            int LA129_0 = input.LA(1);

            if ( (LA129_0==64) ) {
                alt129=1;
            }
            switch (alt129) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:854:35: '?' expression ':' expression
                    {
                    match(input,64,FOLLOW_64_in_conditionalExpression4428); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_conditionalExpression4430);
                    expression();
                    _fsp--;
                    if (failed) return ;
                    match(input,75,FOLLOW_75_in_conditionalExpression4432); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_conditionalExpression4434);
                    expression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 107, conditionalExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end conditionalExpression


    // $ANTLR start conditionalOrExpression
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:857:1: conditionalOrExpression : conditionalAndExpression ( '||' conditionalAndExpression )* ;
    public final void conditionalOrExpression() throws RecognitionException {
        int conditionalOrExpression_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 108) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:858:5: ( conditionalAndExpression ( '||' conditionalAndExpression )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:858:9: conditionalAndExpression ( '||' conditionalAndExpression )*
            {
            pushFollow(FOLLOW_conditionalAndExpression_in_conditionalOrExpression4456);
            conditionalAndExpression();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:858:34: ( '||' conditionalAndExpression )*
            loop130:
            do {
                int alt130=2;
                int LA130_0 = input.LA(1);

                if ( (LA130_0==98) ) {
                    alt130=1;
                }


                switch (alt130) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:858:36: '||' conditionalAndExpression
            	    {
            	    match(input,98,FOLLOW_98_in_conditionalOrExpression4460); if (failed) return ;
            	    pushFollow(FOLLOW_conditionalAndExpression_in_conditionalOrExpression4462);
            	    conditionalAndExpression();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop130;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 108, conditionalOrExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end conditionalOrExpression


    // $ANTLR start conditionalAndExpression
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:861:1: conditionalAndExpression : inclusiveOrExpression ( '&&' inclusiveOrExpression )* ;
    public final void conditionalAndExpression() throws RecognitionException {
        int conditionalAndExpression_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 109) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:862:5: ( inclusiveOrExpression ( '&&' inclusiveOrExpression )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:862:9: inclusiveOrExpression ( '&&' inclusiveOrExpression )*
            {
            pushFollow(FOLLOW_inclusiveOrExpression_in_conditionalAndExpression4484);
            inclusiveOrExpression();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:862:31: ( '&&' inclusiveOrExpression )*
            loop131:
            do {
                int alt131=2;
                int LA131_0 = input.LA(1);

                if ( (LA131_0==99) ) {
                    alt131=1;
                }


                switch (alt131) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:862:33: '&&' inclusiveOrExpression
            	    {
            	    match(input,99,FOLLOW_99_in_conditionalAndExpression4488); if (failed) return ;
            	    pushFollow(FOLLOW_inclusiveOrExpression_in_conditionalAndExpression4490);
            	    inclusiveOrExpression();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop131;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 109, conditionalAndExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end conditionalAndExpression


    // $ANTLR start inclusiveOrExpression
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:865:1: inclusiveOrExpression : exclusiveOrExpression ( '|' exclusiveOrExpression )* ;
    public final void inclusiveOrExpression() throws RecognitionException {
        int inclusiveOrExpression_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 110) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:866:5: ( exclusiveOrExpression ( '|' exclusiveOrExpression )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:866:9: exclusiveOrExpression ( '|' exclusiveOrExpression )*
            {
            pushFollow(FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression4512);
            exclusiveOrExpression();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:866:31: ( '|' exclusiveOrExpression )*
            loop132:
            do {
                int alt132=2;
                int LA132_0 = input.LA(1);

                if ( (LA132_0==100) ) {
                    alt132=1;
                }


                switch (alt132) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:866:33: '|' exclusiveOrExpression
            	    {
            	    match(input,100,FOLLOW_100_in_inclusiveOrExpression4516); if (failed) return ;
            	    pushFollow(FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression4518);
            	    exclusiveOrExpression();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop132;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 110, inclusiveOrExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end inclusiveOrExpression


    // $ANTLR start exclusiveOrExpression
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:869:1: exclusiveOrExpression : andExpression ( '^' andExpression )* ;
    public final void exclusiveOrExpression() throws RecognitionException {
        int exclusiveOrExpression_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 111) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:870:5: ( andExpression ( '^' andExpression )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:870:9: andExpression ( '^' andExpression )*
            {
            pushFollow(FOLLOW_andExpression_in_exclusiveOrExpression4540);
            andExpression();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:870:23: ( '^' andExpression )*
            loop133:
            do {
                int alt133=2;
                int LA133_0 = input.LA(1);

                if ( (LA133_0==101) ) {
                    alt133=1;
                }


                switch (alt133) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:870:25: '^' andExpression
            	    {
            	    match(input,101,FOLLOW_101_in_exclusiveOrExpression4544); if (failed) return ;
            	    pushFollow(FOLLOW_andExpression_in_exclusiveOrExpression4546);
            	    andExpression();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop133;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 111, exclusiveOrExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end exclusiveOrExpression


    // $ANTLR start andExpression
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:873:1: andExpression : equalityExpression ( '&' equalityExpression )* ;
    public final void andExpression() throws RecognitionException {
        int andExpression_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 112) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:874:5: ( equalityExpression ( '&' equalityExpression )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:874:9: equalityExpression ( '&' equalityExpression )*
            {
            pushFollow(FOLLOW_equalityExpression_in_andExpression4568);
            equalityExpression();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:874:28: ( '&' equalityExpression )*
            loop134:
            do {
                int alt134=2;
                int LA134_0 = input.LA(1);

                if ( (LA134_0==43) ) {
                    alt134=1;
                }


                switch (alt134) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:874:30: '&' equalityExpression
            	    {
            	    match(input,43,FOLLOW_43_in_andExpression4572); if (failed) return ;
            	    pushFollow(FOLLOW_equalityExpression_in_andExpression4574);
            	    equalityExpression();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop134;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 112, andExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end andExpression


    // $ANTLR start equalityExpression
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:877:1: equalityExpression : instanceOfExpression ( ( '==' | '!=' ) instanceOfExpression )* ;
    public final void equalityExpression() throws RecognitionException {
        int equalityExpression_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 113) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:878:5: ( instanceOfExpression ( ( '==' | '!=' ) instanceOfExpression )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:878:9: instanceOfExpression ( ( '==' | '!=' ) instanceOfExpression )*
            {
            pushFollow(FOLLOW_instanceOfExpression_in_equalityExpression4596);
            instanceOfExpression();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:878:30: ( ( '==' | '!=' ) instanceOfExpression )*
            loop135:
            do {
                int alt135=2;
                int LA135_0 = input.LA(1);

                if ( ((LA135_0>=102 && LA135_0<=103)) ) {
                    alt135=1;
                }


                switch (alt135) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:878:32: ( '==' | '!=' ) instanceOfExpression
            	    {
            	    if ( (input.LA(1)>=102 && input.LA(1)<=103) ) {
            	        input.consume();
            	        errorRecovery=false;failed=false;
            	    }
            	    else {
            	        if (backtracking>0) {failed=true; return ;}
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_equalityExpression4600);    throw mse;
            	    }

            	    pushFollow(FOLLOW_instanceOfExpression_in_equalityExpression4608);
            	    instanceOfExpression();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop135;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 113, equalityExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end equalityExpression


    // $ANTLR start instanceOfExpression
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:881:1: instanceOfExpression : relationalExpression ( 'instanceof' type )? ;
    public final void instanceOfExpression() throws RecognitionException {
        int instanceOfExpression_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 114) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:882:5: ( relationalExpression ( 'instanceof' type )? )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:882:9: relationalExpression ( 'instanceof' type )?
            {
            pushFollow(FOLLOW_relationalExpression_in_instanceOfExpression4630);
            relationalExpression();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:882:30: ( 'instanceof' type )?
            int alt136=2;
            int LA136_0 = input.LA(1);

            if ( (LA136_0==104) ) {
                alt136=1;
            }
            switch (alt136) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:882:31: 'instanceof' type
                    {
                    match(input,104,FOLLOW_104_in_instanceOfExpression4633); if (failed) return ;
                    pushFollow(FOLLOW_type_in_instanceOfExpression4635);
                    type();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 114, instanceOfExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end instanceOfExpression


    // $ANTLR start relationalExpression
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:885:1: relationalExpression : shiftExpression ( relationalOp shiftExpression )* ;
    public final void relationalExpression() throws RecognitionException {
        int relationalExpression_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 115) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:886:5: ( shiftExpression ( relationalOp shiftExpression )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:886:9: shiftExpression ( relationalOp shiftExpression )*
            {
            pushFollow(FOLLOW_shiftExpression_in_relationalExpression4656);
            shiftExpression();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:886:25: ( relationalOp shiftExpression )*
            loop137:
            do {
                int alt137=2;
                int LA137_0 = input.LA(1);

                if ( (LA137_0==40) ) {
                    int LA137_23 = input.LA(2);

                    if ( (LA137_23==Identifier||(LA137_23>=FloatingPointLiteral && LA137_23<=DecimalLiteral)||LA137_23==47||LA137_23==51||(LA137_23>=56 && LA137_23<=63)||(LA137_23>=65 && LA137_23<=66)||(LA137_23>=69 && LA137_23<=72)||(LA137_23>=105 && LA137_23<=106)||(LA137_23>=109 && LA137_23<=113)) ) {
                        alt137=1;
                    }


                }
                else if ( (LA137_0==42) ) {
                    int LA137_24 = input.LA(2);

                    if ( (LA137_24==Identifier||(LA137_24>=FloatingPointLiteral && LA137_24<=DecimalLiteral)||LA137_24==47||LA137_24==51||(LA137_24>=56 && LA137_24<=63)||(LA137_24>=65 && LA137_24<=66)||(LA137_24>=69 && LA137_24<=72)||(LA137_24>=105 && LA137_24<=106)||(LA137_24>=109 && LA137_24<=113)) ) {
                        alt137=1;
                    }


                }


                switch (alt137) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:886:27: relationalOp shiftExpression
            	    {
            	    pushFollow(FOLLOW_relationalOp_in_relationalExpression4660);
            	    relationalOp();
            	    _fsp--;
            	    if (failed) return ;
            	    pushFollow(FOLLOW_shiftExpression_in_relationalExpression4662);
            	    shiftExpression();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop137;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 115, relationalExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end relationalExpression


    // $ANTLR start relationalOp
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:889:1: relationalOp : ( ( '<' '=' )=>t1= '<' t2= '=' {...}? | ( '>' '=' )=>t1= '>' t2= '=' {...}? | '<' | '>' );
    public final void relationalOp() throws RecognitionException {
        int relationalOp_StartIndex = input.index();
        Token t1=null;
        Token t2=null;

        try {
            if ( backtracking>0 && alreadyParsedRule(input, 116) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:890:5: ( ( '<' '=' )=>t1= '<' t2= '=' {...}? | ( '>' '=' )=>t1= '>' t2= '=' {...}? | '<' | '>' )
            int alt138=4;
            int LA138_0 = input.LA(1);

            if ( (LA138_0==40) ) {
                int LA138_1 = input.LA(2);

                if ( (LA138_1==51) && (synpred211())) {
                    alt138=1;
                }
                else if ( (LA138_1==Identifier||(LA138_1>=FloatingPointLiteral && LA138_1<=DecimalLiteral)||LA138_1==47||(LA138_1>=56 && LA138_1<=63)||(LA138_1>=65 && LA138_1<=66)||(LA138_1>=69 && LA138_1<=72)||(LA138_1>=105 && LA138_1<=106)||(LA138_1>=109 && LA138_1<=113)) ) {
                    alt138=3;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("889:1: relationalOp : ( ( '<' '=' )=>t1= '<' t2= '=' {...}? | ( '>' '=' )=>t1= '>' t2= '=' {...}? | '<' | '>' );", 138, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA138_0==42) ) {
                int LA138_2 = input.LA(2);

                if ( (LA138_2==51) && (synpred212())) {
                    alt138=2;
                }
                else if ( (LA138_2==Identifier||(LA138_2>=FloatingPointLiteral && LA138_2<=DecimalLiteral)||LA138_2==47||(LA138_2>=56 && LA138_2<=63)||(LA138_2>=65 && LA138_2<=66)||(LA138_2>=69 && LA138_2<=72)||(LA138_2>=105 && LA138_2<=106)||(LA138_2>=109 && LA138_2<=113)) ) {
                    alt138=4;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("889:1: relationalOp : ( ( '<' '=' )=>t1= '<' t2= '=' {...}? | ( '>' '=' )=>t1= '>' t2= '=' {...}? | '<' | '>' );", 138, 2, input);

                    throw nvae;
                }
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("889:1: relationalOp : ( ( '<' '=' )=>t1= '<' t2= '=' {...}? | ( '>' '=' )=>t1= '>' t2= '=' {...}? | '<' | '>' );", 138, 0, input);

                throw nvae;
            }
            switch (alt138) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:890:9: ( '<' '=' )=>t1= '<' t2= '=' {...}?
                    {
                    t1=(Token)input.LT(1);
                    match(input,40,FOLLOW_40_in_relationalOp4697); if (failed) return ;
                    t2=(Token)input.LT(1);
                    match(input,51,FOLLOW_51_in_relationalOp4701); if (failed) return ;
                    if ( !( t1.getLine() == t2.getLine() && 
                              t1.getCharPositionInLine() + 1 == t2.getCharPositionInLine() ) ) {
                        if (backtracking>0) {failed=true; return ;}
                        throw new FailedPredicateException(input, "relationalOp", " $t1.getLine() == $t2.getLine() && \n          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() ");
                    }

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:893:9: ( '>' '=' )=>t1= '>' t2= '=' {...}?
                    {
                    t1=(Token)input.LT(1);
                    match(input,42,FOLLOW_42_in_relationalOp4731); if (failed) return ;
                    t2=(Token)input.LT(1);
                    match(input,51,FOLLOW_51_in_relationalOp4735); if (failed) return ;
                    if ( !( t1.getLine() == t2.getLine() && 
                              t1.getCharPositionInLine() + 1 == t2.getCharPositionInLine() ) ) {
                        if (backtracking>0) {failed=true; return ;}
                        throw new FailedPredicateException(input, "relationalOp", " $t1.getLine() == $t2.getLine() && \n          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() ");
                    }

                    }
                    break;
                case 3 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:896:9: '<'
                    {
                    match(input,40,FOLLOW_40_in_relationalOp4756); if (failed) return ;

                    }
                    break;
                case 4 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:897:9: '>'
                    {
                    match(input,42,FOLLOW_42_in_relationalOp4767); if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 116, relationalOp_StartIndex); }
        }
        return ;
    }
    // $ANTLR end relationalOp


    // $ANTLR start shiftExpression
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:900:1: shiftExpression : additiveExpression ( shiftOp additiveExpression )* ;
    public final void shiftExpression() throws RecognitionException {
        int shiftExpression_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 117) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:901:5: ( additiveExpression ( shiftOp additiveExpression )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:901:9: additiveExpression ( shiftOp additiveExpression )*
            {
            pushFollow(FOLLOW_additiveExpression_in_shiftExpression4787);
            additiveExpression();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:901:28: ( shiftOp additiveExpression )*
            loop139:
            do {
                int alt139=2;
                int LA139_0 = input.LA(1);

                if ( (LA139_0==40) ) {
                    int LA139_1 = input.LA(2);

                    if ( (LA139_1==40) ) {
                        int LA139_27 = input.LA(3);

                        if ( (synpred214()) ) {
                            alt139=1;
                        }


                    }


                }
                else if ( (LA139_0==42) ) {
                    int LA139_2 = input.LA(2);

                    if ( (LA139_2==42) ) {
                        int LA139_48 = input.LA(3);

                        if ( (synpred214()) ) {
                            alt139=1;
                        }


                    }


                }


                switch (alt139) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:901:30: shiftOp additiveExpression
            	    {
            	    pushFollow(FOLLOW_shiftOp_in_shiftExpression4791);
            	    shiftOp();
            	    _fsp--;
            	    if (failed) return ;
            	    pushFollow(FOLLOW_additiveExpression_in_shiftExpression4793);
            	    additiveExpression();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop139;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 117, shiftExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end shiftExpression


    // $ANTLR start shiftOp
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:904:1: shiftOp : ( ( '<' '<' )=>t1= '<' t2= '<' {...}? | ( '>' '>' '>' )=>t1= '>' t2= '>' t3= '>' {...}? | ( '>' '>' )=>t1= '>' t2= '>' {...}?);
    public final void shiftOp() throws RecognitionException {
        int shiftOp_StartIndex = input.index();
        Token t1=null;
        Token t2=null;
        Token t3=null;

        try {
            if ( backtracking>0 && alreadyParsedRule(input, 118) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:905:5: ( ( '<' '<' )=>t1= '<' t2= '<' {...}? | ( '>' '>' '>' )=>t1= '>' t2= '>' t3= '>' {...}? | ( '>' '>' )=>t1= '>' t2= '>' {...}?)
            int alt140=3;
            int LA140_0 = input.LA(1);

            if ( (LA140_0==40) && (synpred215())) {
                alt140=1;
            }
            else if ( (LA140_0==42) ) {
                int LA140_2 = input.LA(2);

                if ( (LA140_2==42) ) {
                    int LA140_3 = input.LA(3);

                    if ( (synpred216()) ) {
                        alt140=2;
                    }
                    else if ( (synpred217()) ) {
                        alt140=3;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("904:1: shiftOp : ( ( '<' '<' )=>t1= '<' t2= '<' {...}? | ( '>' '>' '>' )=>t1= '>' t2= '>' t3= '>' {...}? | ( '>' '>' )=>t1= '>' t2= '>' {...}?);", 140, 3, input);

                        throw nvae;
                    }
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("904:1: shiftOp : ( ( '<' '<' )=>t1= '<' t2= '<' {...}? | ( '>' '>' '>' )=>t1= '>' t2= '>' t3= '>' {...}? | ( '>' '>' )=>t1= '>' t2= '>' {...}?);", 140, 2, input);

                    throw nvae;
                }
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("904:1: shiftOp : ( ( '<' '<' )=>t1= '<' t2= '<' {...}? | ( '>' '>' '>' )=>t1= '>' t2= '>' t3= '>' {...}? | ( '>' '>' )=>t1= '>' t2= '>' {...}?);", 140, 0, input);

                throw nvae;
            }
            switch (alt140) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:905:9: ( '<' '<' )=>t1= '<' t2= '<' {...}?
                    {
                    t1=(Token)input.LT(1);
                    match(input,40,FOLLOW_40_in_shiftOp4824); if (failed) return ;
                    t2=(Token)input.LT(1);
                    match(input,40,FOLLOW_40_in_shiftOp4828); if (failed) return ;
                    if ( !( t1.getLine() == t2.getLine() && 
                              t1.getCharPositionInLine() + 1 == t2.getCharPositionInLine() ) ) {
                        if (backtracking>0) {failed=true; return ;}
                        throw new FailedPredicateException(input, "shiftOp", " $t1.getLine() == $t2.getLine() && \n          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() ");
                    }

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:908:9: ( '>' '>' '>' )=>t1= '>' t2= '>' t3= '>' {...}?
                    {
                    t1=(Token)input.LT(1);
                    match(input,42,FOLLOW_42_in_shiftOp4860); if (failed) return ;
                    t2=(Token)input.LT(1);
                    match(input,42,FOLLOW_42_in_shiftOp4864); if (failed) return ;
                    t3=(Token)input.LT(1);
                    match(input,42,FOLLOW_42_in_shiftOp4868); if (failed) return ;
                    if ( !( t1.getLine() == t2.getLine() && 
                              t1.getCharPositionInLine() + 1 == t2.getCharPositionInLine() &&
                              t2.getLine() == t3.getLine() && 
                              t2.getCharPositionInLine() + 1 == t3.getCharPositionInLine() ) ) {
                        if (backtracking>0) {failed=true; return ;}
                        throw new FailedPredicateException(input, "shiftOp", " $t1.getLine() == $t2.getLine() && \n          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() &&\n          $t2.getLine() == $t3.getLine() && \n          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() ");
                    }

                    }
                    break;
                case 3 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:913:9: ( '>' '>' )=>t1= '>' t2= '>' {...}?
                    {
                    t1=(Token)input.LT(1);
                    match(input,42,FOLLOW_42_in_shiftOp4898); if (failed) return ;
                    t2=(Token)input.LT(1);
                    match(input,42,FOLLOW_42_in_shiftOp4902); if (failed) return ;
                    if ( !( t1.getLine() == t2.getLine() && 
                              t1.getCharPositionInLine() + 1 == t2.getCharPositionInLine() ) ) {
                        if (backtracking>0) {failed=true; return ;}
                        throw new FailedPredicateException(input, "shiftOp", " $t1.getLine() == $t2.getLine() && \n          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() ");
                    }

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 118, shiftOp_StartIndex); }
        }
        return ;
    }
    // $ANTLR end shiftOp


    // $ANTLR start additiveExpression
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:919:1: additiveExpression : multiplicativeExpression ( ( '+' | '-' ) multiplicativeExpression )* ;
    public final void additiveExpression() throws RecognitionException {
        int additiveExpression_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 119) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:920:5: ( multiplicativeExpression ( ( '+' | '-' ) multiplicativeExpression )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:920:9: multiplicativeExpression ( ( '+' | '-' ) multiplicativeExpression )*
            {
            pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression4932);
            multiplicativeExpression();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:920:34: ( ( '+' | '-' ) multiplicativeExpression )*
            loop141:
            do {
                int alt141=2;
                int LA141_0 = input.LA(1);

                if ( ((LA141_0>=105 && LA141_0<=106)) ) {
                    alt141=1;
                }


                switch (alt141) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:920:36: ( '+' | '-' ) multiplicativeExpression
            	    {
            	    if ( (input.LA(1)>=105 && input.LA(1)<=106) ) {
            	        input.consume();
            	        errorRecovery=false;failed=false;
            	    }
            	    else {
            	        if (backtracking>0) {failed=true; return ;}
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_additiveExpression4936);    throw mse;
            	    }

            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression4944);
            	    multiplicativeExpression();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop141;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 119, additiveExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end additiveExpression


    // $ANTLR start multiplicativeExpression
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:923:1: multiplicativeExpression : unaryExpression ( ( '*' | '/' | '%' ) unaryExpression )* ;
    public final void multiplicativeExpression() throws RecognitionException {
        int multiplicativeExpression_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 120) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:924:5: ( unaryExpression ( ( '*' | '/' | '%' ) unaryExpression )* )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:924:9: unaryExpression ( ( '*' | '/' | '%' ) unaryExpression )*
            {
            pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4966);
            unaryExpression();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:924:25: ( ( '*' | '/' | '%' ) unaryExpression )*
            loop142:
            do {
                int alt142=2;
                int LA142_0 = input.LA(1);

                if ( (LA142_0==30||(LA142_0>=107 && LA142_0<=108)) ) {
                    alt142=1;
                }


                switch (alt142) {
            	case 1 :
            	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:924:27: ( '*' | '/' | '%' ) unaryExpression
            	    {
            	    if ( input.LA(1)==30||(input.LA(1)>=107 && input.LA(1)<=108) ) {
            	        input.consume();
            	        errorRecovery=false;failed=false;
            	    }
            	    else {
            	        if (backtracking>0) {failed=true; return ;}
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_multiplicativeExpression4970);    throw mse;
            	    }

            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4984);
            	    unaryExpression();
            	    _fsp--;
            	    if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop142;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 120, multiplicativeExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end multiplicativeExpression


    // $ANTLR start unaryExpression
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:927:1: unaryExpression : ( '+' unaryExpression | '-' unaryExpression | '++' unaryExpression | '--' unaryExpression | unaryExpressionNotPlusMinus );
    public final void unaryExpression() throws RecognitionException {
        int unaryExpression_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 121) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:928:5: ( '+' unaryExpression | '-' unaryExpression | '++' unaryExpression | '--' unaryExpression | unaryExpressionNotPlusMinus )
            int alt143=5;
            switch ( input.LA(1) ) {
            case 105:
                {
                alt143=1;
                }
                break;
            case 106:
                {
                alt143=2;
                }
                break;
            case 109:
                {
                alt143=3;
                }
                break;
            case 110:
                {
                alt143=4;
                }
                break;
            case Identifier:
            case FloatingPointLiteral:
            case CharacterLiteral:
            case StringLiteral:
            case HexLiteral:
            case OctalLiteral:
            case DecimalLiteral:
            case 47:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 65:
            case 66:
            case 69:
            case 70:
            case 71:
            case 72:
            case 111:
            case 112:
            case 113:
                {
                alt143=5;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("927:1: unaryExpression : ( '+' unaryExpression | '-' unaryExpression | '++' unaryExpression | '--' unaryExpression | unaryExpressionNotPlusMinus );", 143, 0, input);

                throw nvae;
            }

            switch (alt143) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:928:9: '+' unaryExpression
                    {
                    match(input,105,FOLLOW_105_in_unaryExpression5010); if (failed) return ;
                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression5012);
                    unaryExpression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:929:9: '-' unaryExpression
                    {
                    match(input,106,FOLLOW_106_in_unaryExpression5022); if (failed) return ;
                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression5024);
                    unaryExpression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 3 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:930:9: '++' unaryExpression
                    {
                    match(input,109,FOLLOW_109_in_unaryExpression5034); if (failed) return ;
                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression5036);
                    unaryExpression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 4 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:931:9: '--' unaryExpression
                    {
                    match(input,110,FOLLOW_110_in_unaryExpression5046); if (failed) return ;
                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression5048);
                    unaryExpression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 5 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:932:9: unaryExpressionNotPlusMinus
                    {
                    pushFollow(FOLLOW_unaryExpressionNotPlusMinus_in_unaryExpression5058);
                    unaryExpressionNotPlusMinus();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 121, unaryExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end unaryExpression


    // $ANTLR start unaryExpressionNotPlusMinus
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:935:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );
    public final void unaryExpressionNotPlusMinus() throws RecognitionException {
        int unaryExpressionNotPlusMinus_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 122) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:936:5: ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? )
            int alt146=4;
            switch ( input.LA(1) ) {
            case 111:
                {
                alt146=1;
                }
                break;
            case 112:
                {
                alt146=2;
                }
                break;
            case 66:
                {
                switch ( input.LA(2) ) {
                case Identifier:
                    {
                    int LA146_16 = input.LA(3);

                    if ( (synpred229()) ) {
                        alt146=3;
                    }
                    else if ( (true) ) {
                        alt146=4;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("935:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );", 146, 16, input);

                        throw nvae;
                    }
                    }
                    break;
                case 56:
                case 57:
                case 58:
                case 59:
                case 60:
                case 61:
                case 62:
                case 63:
                    {
                    int LA146_17 = input.LA(3);

                    if ( (synpred229()) ) {
                        alt146=3;
                    }
                    else if ( (true) ) {
                        alt146=4;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("935:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );", 146, 17, input);

                        throw nvae;
                    }
                    }
                    break;
                case 105:
                    {
                    int LA146_18 = input.LA(3);

                    if ( (synpred229()) ) {
                        alt146=3;
                    }
                    else if ( (true) ) {
                        alt146=4;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("935:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );", 146, 18, input);

                        throw nvae;
                    }
                    }
                    break;
                case 106:
                    {
                    int LA146_19 = input.LA(3);

                    if ( (synpred229()) ) {
                        alt146=3;
                    }
                    else if ( (true) ) {
                        alt146=4;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("935:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );", 146, 19, input);

                        throw nvae;
                    }
                    }
                    break;
                case 109:
                    {
                    int LA146_20 = input.LA(3);

                    if ( (synpred229()) ) {
                        alt146=3;
                    }
                    else if ( (true) ) {
                        alt146=4;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("935:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );", 146, 20, input);

                        throw nvae;
                    }
                    }
                    break;
                case 110:
                    {
                    int LA146_21 = input.LA(3);

                    if ( (synpred229()) ) {
                        alt146=3;
                    }
                    else if ( (true) ) {
                        alt146=4;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("935:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );", 146, 21, input);

                        throw nvae;
                    }
                    }
                    break;
                case 111:
                    {
                    int LA146_22 = input.LA(3);

                    if ( (synpred229()) ) {
                        alt146=3;
                    }
                    else if ( (true) ) {
                        alt146=4;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("935:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );", 146, 22, input);

                        throw nvae;
                    }
                    }
                    break;
                case 112:
                    {
                    int LA146_23 = input.LA(3);

                    if ( (synpred229()) ) {
                        alt146=3;
                    }
                    else if ( (true) ) {
                        alt146=4;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("935:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );", 146, 23, input);

                        throw nvae;
                    }
                    }
                    break;
                case 66:
                    {
                    int LA146_24 = input.LA(3);

                    if ( (synpred229()) ) {
                        alt146=3;
                    }
                    else if ( (true) ) {
                        alt146=4;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("935:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );", 146, 24, input);

                        throw nvae;
                    }
                    }
                    break;
                case 69:
                    {
                    int LA146_25 = input.LA(3);

                    if ( (synpred229()) ) {
                        alt146=3;
                    }
                    else if ( (true) ) {
                        alt146=4;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("935:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );", 146, 25, input);

                        throw nvae;
                    }
                    }
                    break;
                case 65:
                    {
                    int LA146_26 = input.LA(3);

                    if ( (synpred229()) ) {
                        alt146=3;
                    }
                    else if ( (true) ) {
                        alt146=4;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("935:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );", 146, 26, input);

                        throw nvae;
                    }
                    }
                    break;
                case HexLiteral:
                case OctalLiteral:
                case DecimalLiteral:
                    {
                    int LA146_27 = input.LA(3);

                    if ( (synpred229()) ) {
                        alt146=3;
                    }
                    else if ( (true) ) {
                        alt146=4;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("935:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );", 146, 27, input);

                        throw nvae;
                    }
                    }
                    break;
                case FloatingPointLiteral:
                    {
                    int LA146_28 = input.LA(3);

                    if ( (synpred229()) ) {
                        alt146=3;
                    }
                    else if ( (true) ) {
                        alt146=4;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("935:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );", 146, 28, input);

                        throw nvae;
                    }
                    }
                    break;
                case CharacterLiteral:
                    {
                    int LA146_29 = input.LA(3);

                    if ( (synpred229()) ) {
                        alt146=3;
                    }
                    else if ( (true) ) {
                        alt146=4;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("935:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );", 146, 29, input);

                        throw nvae;
                    }
                    }
                    break;
                case StringLiteral:
                    {
                    int LA146_30 = input.LA(3);

                    if ( (synpred229()) ) {
                        alt146=3;
                    }
                    else if ( (true) ) {
                        alt146=4;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("935:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );", 146, 30, input);

                        throw nvae;
                    }
                    }
                    break;
                case 71:
                case 72:
                    {
                    int LA146_31 = input.LA(3);

                    if ( (synpred229()) ) {
                        alt146=3;
                    }
                    else if ( (true) ) {
                        alt146=4;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("935:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );", 146, 31, input);

                        throw nvae;
                    }
                    }
                    break;
                case 70:
                    {
                    int LA146_32 = input.LA(3);

                    if ( (synpred229()) ) {
                        alt146=3;
                    }
                    else if ( (true) ) {
                        alt146=4;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("935:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );", 146, 32, input);

                        throw nvae;
                    }
                    }
                    break;
                case 113:
                    {
                    int LA146_33 = input.LA(3);

                    if ( (synpred229()) ) {
                        alt146=3;
                    }
                    else if ( (true) ) {
                        alt146=4;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("935:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );", 146, 33, input);

                        throw nvae;
                    }
                    }
                    break;
                case 47:
                    {
                    int LA146_34 = input.LA(3);

                    if ( (synpred229()) ) {
                        alt146=3;
                    }
                    else if ( (true) ) {
                        alt146=4;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("935:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );", 146, 34, input);

                        throw nvae;
                    }
                    }
                    break;
                default:
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("935:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );", 146, 3, input);

                    throw nvae;
                }

                }
                break;
            case Identifier:
            case FloatingPointLiteral:
            case CharacterLiteral:
            case StringLiteral:
            case HexLiteral:
            case OctalLiteral:
            case DecimalLiteral:
            case 47:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 65:
            case 69:
            case 70:
            case 71:
            case 72:
            case 113:
                {
                alt146=4;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("935:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );", 146, 0, input);

                throw nvae;
            }

            switch (alt146) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:936:9: '~' unaryExpression
                    {
                    match(input,111,FOLLOW_111_in_unaryExpressionNotPlusMinus5077); if (failed) return ;
                    pushFollow(FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus5079);
                    unaryExpression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:937:9: '!' unaryExpression
                    {
                    match(input,112,FOLLOW_112_in_unaryExpressionNotPlusMinus5089); if (failed) return ;
                    pushFollow(FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus5091);
                    unaryExpression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 3 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:938:9: castExpression
                    {
                    pushFollow(FOLLOW_castExpression_in_unaryExpressionNotPlusMinus5101);
                    castExpression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 4 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:939:9: primary ( selector )* ( '++' | '--' )?
                    {
                    pushFollow(FOLLOW_primary_in_unaryExpressionNotPlusMinus5111);
                    primary();
                    _fsp--;
                    if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:939:17: ( selector )*
                    loop144:
                    do {
                        int alt144=2;
                        int LA144_0 = input.LA(1);

                        if ( (LA144_0==29||LA144_0==48) ) {
                            alt144=1;
                        }


                        switch (alt144) {
                    	case 1 :
                    	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: selector
                    	    {
                    	    pushFollow(FOLLOW_selector_in_unaryExpressionNotPlusMinus5113);
                    	    selector();
                    	    _fsp--;
                    	    if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop144;
                        }
                    } while (true);

                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:939:27: ( '++' | '--' )?
                    int alt145=2;
                    int LA145_0 = input.LA(1);

                    if ( ((LA145_0>=109 && LA145_0<=110)) ) {
                        alt145=1;
                    }
                    switch (alt145) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:
                            {
                            if ( (input.LA(1)>=109 && input.LA(1)<=110) ) {
                                input.consume();
                                errorRecovery=false;failed=false;
                            }
                            else {
                                if (backtracking>0) {failed=true; return ;}
                                MismatchedSetException mse =
                                    new MismatchedSetException(null,input);
                                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_unaryExpressionNotPlusMinus5116);    throw mse;
                            }


                            }
                            break;

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 122, unaryExpressionNotPlusMinus_StartIndex); }
        }
        return ;
    }
    // $ANTLR end unaryExpressionNotPlusMinus


    // $ANTLR start castExpression
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:942:1: castExpression : ( '(' primitiveType ')' unaryExpression | '(' ( type | expression ) ')' unaryExpressionNotPlusMinus );
    public final void castExpression() throws RecognitionException {
        int castExpression_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 123) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:943:5: ( '(' primitiveType ')' unaryExpression | '(' ( type | expression ) ')' unaryExpressionNotPlusMinus )
            int alt148=2;
            int LA148_0 = input.LA(1);

            if ( (LA148_0==66) ) {
                int LA148_1 = input.LA(2);

                if ( (LA148_1==Identifier||(LA148_1>=FloatingPointLiteral && LA148_1<=DecimalLiteral)||LA148_1==47||(LA148_1>=65 && LA148_1<=66)||(LA148_1>=69 && LA148_1<=72)||(LA148_1>=105 && LA148_1<=106)||(LA148_1>=109 && LA148_1<=113)) ) {
                    alt148=2;
                }
                else if ( ((LA148_1>=56 && LA148_1<=63)) ) {
                    int LA148_3 = input.LA(3);

                    if ( (synpred233()) ) {
                        alt148=1;
                    }
                    else if ( (true) ) {
                        alt148=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("942:1: castExpression : ( '(' primitiveType ')' unaryExpression | '(' ( type | expression ) ')' unaryExpressionNotPlusMinus );", 148, 3, input);

                        throw nvae;
                    }
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("942:1: castExpression : ( '(' primitiveType ')' unaryExpression | '(' ( type | expression ) ')' unaryExpressionNotPlusMinus );", 148, 1, input);

                    throw nvae;
                }
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("942:1: castExpression : ( '(' primitiveType ')' unaryExpression | '(' ( type | expression ) ')' unaryExpressionNotPlusMinus );", 148, 0, input);

                throw nvae;
            }
            switch (alt148) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:943:8: '(' primitiveType ')' unaryExpression
                    {
                    match(input,66,FOLLOW_66_in_castExpression5139); if (failed) return ;
                    pushFollow(FOLLOW_primitiveType_in_castExpression5141);
                    primitiveType();
                    _fsp--;
                    if (failed) return ;
                    match(input,67,FOLLOW_67_in_castExpression5143); if (failed) return ;
                    pushFollow(FOLLOW_unaryExpression_in_castExpression5145);
                    unaryExpression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:944:8: '(' ( type | expression ) ')' unaryExpressionNotPlusMinus
                    {
                    match(input,66,FOLLOW_66_in_castExpression5154); if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:944:12: ( type | expression )
                    int alt147=2;
                    switch ( input.LA(1) ) {
                    case Identifier:
                        {
                        int LA147_1 = input.LA(2);

                        if ( (synpred234()) ) {
                            alt147=1;
                        }
                        else if ( (true) ) {
                            alt147=2;
                        }
                        else {
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("944:12: ( type | expression )", 147, 1, input);

                            throw nvae;
                        }
                        }
                        break;
                    case 56:
                    case 57:
                    case 58:
                    case 59:
                    case 60:
                    case 61:
                    case 62:
                    case 63:
                        {
                        switch ( input.LA(2) ) {
                        case 48:
                            {
                            int LA147_47 = input.LA(3);

                            if ( (synpred234()) ) {
                                alt147=1;
                            }
                            else if ( (true) ) {
                                alt147=2;
                            }
                            else {
                                if (backtracking>0) {failed=true; return ;}
                                NoViableAltException nvae =
                                    new NoViableAltException("944:12: ( type | expression )", 147, 47, input);

                                throw nvae;
                            }
                            }
                            break;
                        case 29:
                            {
                            alt147=2;
                            }
                            break;
                        case 67:
                            {
                            alt147=1;
                            }
                            break;
                        default:
                            if (backtracking>0) {failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("944:12: ( type | expression )", 147, 2, input);

                            throw nvae;
                        }

                        }
                        break;
                    case FloatingPointLiteral:
                    case CharacterLiteral:
                    case StringLiteral:
                    case HexLiteral:
                    case OctalLiteral:
                    case DecimalLiteral:
                    case 47:
                    case 65:
                    case 66:
                    case 69:
                    case 70:
                    case 71:
                    case 72:
                    case 105:
                    case 106:
                    case 109:
                    case 110:
                    case 111:
                    case 112:
                    case 113:
                        {
                        alt147=2;
                        }
                        break;
                    default:
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("944:12: ( type | expression )", 147, 0, input);

                        throw nvae;
                    }

                    switch (alt147) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:944:13: type
                            {
                            pushFollow(FOLLOW_type_in_castExpression5157);
                            type();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;
                        case 2 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:944:20: expression
                            {
                            pushFollow(FOLLOW_expression_in_castExpression5161);
                            expression();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }

                    match(input,67,FOLLOW_67_in_castExpression5164); if (failed) return ;
                    pushFollow(FOLLOW_unaryExpressionNotPlusMinus_in_castExpression5166);
                    unaryExpressionNotPlusMinus();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 123, castExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end castExpression


    // $ANTLR start primary
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:947:1: primary : ( parExpression | 'this' ( '.' Identifier )* ( identifierSuffix )? | t1= 'super' superSuffix | literal | 'new' creator | Identifier ( '.' Identifier )* ( identifierSuffix )? | primitiveType ( '[' ']' )* '.' 'class' | 'void' '.' 'class' );
    public final void primary() throws RecognitionException {
        int primary_StartIndex = input.index();
        Token t1=null;

        try {
            if ( backtracking>0 && alreadyParsedRule(input, 124) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:948:5: ( parExpression | 'this' ( '.' Identifier )* ( identifierSuffix )? | t1= 'super' superSuffix | literal | 'new' creator | Identifier ( '.' Identifier )* ( identifierSuffix )? | primitiveType ( '[' ']' )* '.' 'class' | 'void' '.' 'class' )
            int alt154=8;
            switch ( input.LA(1) ) {
            case 66:
                {
                alt154=1;
                }
                break;
            case 69:
                {
                alt154=2;
                }
                break;
            case 65:
                {
                alt154=3;
                }
                break;
            case FloatingPointLiteral:
            case CharacterLiteral:
            case StringLiteral:
            case HexLiteral:
            case OctalLiteral:
            case DecimalLiteral:
            case 70:
            case 71:
            case 72:
                {
                alt154=4;
                }
                break;
            case 113:
                {
                alt154=5;
                }
                break;
            case Identifier:
                {
                alt154=6;
                }
                break;
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
                {
                alt154=7;
                }
                break;
            case 47:
                {
                alt154=8;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("947:1: primary : ( parExpression | 'this' ( '.' Identifier )* ( identifierSuffix )? | t1= 'super' superSuffix | literal | 'new' creator | Identifier ( '.' Identifier )* ( identifierSuffix )? | primitiveType ( '[' ']' )* '.' 'class' | 'void' '.' 'class' );", 154, 0, input);

                throw nvae;
            }

            switch (alt154) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:948:9: parExpression
                    {
                    pushFollow(FOLLOW_parExpression_in_primary5185);
                    parExpression();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:949:9: 'this' ( '.' Identifier )* ( identifierSuffix )?
                    {
                    match(input,69,FOLLOW_69_in_primary5195); if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:949:16: ( '.' Identifier )*
                    loop149:
                    do {
                        int alt149=2;
                        int LA149_0 = input.LA(1);

                        if ( (LA149_0==29) ) {
                            int LA149_3 = input.LA(2);

                            if ( (LA149_3==Identifier) ) {
                                int LA149_34 = input.LA(3);

                                if ( (synpred236()) ) {
                                    alt149=1;
                                }


                            }


                        }


                        switch (alt149) {
                    	case 1 :
                    	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:949:17: '.' Identifier
                    	    {
                    	    match(input,29,FOLLOW_29_in_primary5198); if (failed) return ;
                    	    match(input,Identifier,FOLLOW_Identifier_in_primary5200); if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop149;
                        }
                    } while (true);

                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:949:34: ( identifierSuffix )?
                    int alt150=2;
                    switch ( input.LA(1) ) {
                        case 48:
                            {
                            switch ( input.LA(2) ) {
                                case 49:
                                    {
                                    alt150=1;
                                    }
                                    break;
                                case 105:
                                    {
                                    int LA150_34 = input.LA(3);

                                    if ( (synpred237()) ) {
                                        alt150=1;
                                    }
                                    }
                                    break;
                                case 106:
                                    {
                                    int LA150_35 = input.LA(3);

                                    if ( (synpred237()) ) {
                                        alt150=1;
                                    }
                                    }
                                    break;
                                case 109:
                                    {
                                    int LA150_36 = input.LA(3);

                                    if ( (synpred237()) ) {
                                        alt150=1;
                                    }
                                    }
                                    break;
                                case 110:
                                    {
                                    int LA150_37 = input.LA(3);

                                    if ( (synpred237()) ) {
                                        alt150=1;
                                    }
                                    }
                                    break;
                                case 111:
                                    {
                                    int LA150_38 = input.LA(3);

                                    if ( (synpred237()) ) {
                                        alt150=1;
                                    }
                                    }
                                    break;
                                case 112:
                                    {
                                    int LA150_39 = input.LA(3);

                                    if ( (synpred237()) ) {
                                        alt150=1;
                                    }
                                    }
                                    break;
                                case 66:
                                    {
                                    int LA150_40 = input.LA(3);

                                    if ( (synpred237()) ) {
                                        alt150=1;
                                    }
                                    }
                                    break;
                                case 69:
                                    {
                                    int LA150_41 = input.LA(3);

                                    if ( (synpred237()) ) {
                                        alt150=1;
                                    }
                                    }
                                    break;
                                case 65:
                                    {
                                    int LA150_42 = input.LA(3);

                                    if ( (synpred237()) ) {
                                        alt150=1;
                                    }
                                    }
                                    break;
                                case HexLiteral:
                                case OctalLiteral:
                                case DecimalLiteral:
                                    {
                                    int LA150_43 = input.LA(3);

                                    if ( (synpred237()) ) {
                                        alt150=1;
                                    }
                                    }
                                    break;
                                case FloatingPointLiteral:
                                    {
                                    int LA150_44 = input.LA(3);

                                    if ( (synpred237()) ) {
                                        alt150=1;
                                    }
                                    }
                                    break;
                                case CharacterLiteral:
                                    {
                                    int LA150_45 = input.LA(3);

                                    if ( (synpred237()) ) {
                                        alt150=1;
                                    }
                                    }
                                    break;
                                case StringLiteral:
                                    {
                                    int LA150_46 = input.LA(3);

                                    if ( (synpred237()) ) {
                                        alt150=1;
                                    }
                                    }
                                    break;
                                case 71:
                                case 72:
                                    {
                                    int LA150_47 = input.LA(3);

                                    if ( (synpred237()) ) {
                                        alt150=1;
                                    }
                                    }
                                    break;
                                case 70:
                                    {
                                    int LA150_48 = input.LA(3);

                                    if ( (synpred237()) ) {
                                        alt150=1;
                                    }
                                    }
                                    break;
                                case 113:
                                    {
                                    int LA150_49 = input.LA(3);

                                    if ( (synpred237()) ) {
                                        alt150=1;
                                    }
                                    }
                                    break;
                                case Identifier:
                                    {
                                    int LA150_50 = input.LA(3);

                                    if ( (synpred237()) ) {
                                        alt150=1;
                                    }
                                    }
                                    break;
                                case 56:
                                case 57:
                                case 58:
                                case 59:
                                case 60:
                                case 61:
                                case 62:
                                case 63:
                                    {
                                    int LA150_51 = input.LA(3);

                                    if ( (synpred237()) ) {
                                        alt150=1;
                                    }
                                    }
                                    break;
                                case 47:
                                    {
                                    int LA150_52 = input.LA(3);

                                    if ( (synpred237()) ) {
                                        alt150=1;
                                    }
                                    }
                                    break;
                            }

                            }
                            break;
                        case 66:
                            {
                            alt150=1;
                            }
                            break;
                        case 29:
                            {
                            switch ( input.LA(2) ) {
                                case 113:
                                    {
                                    int LA150_53 = input.LA(3);

                                    if ( (synpred237()) ) {
                                        alt150=1;
                                    }
                                    }
                                    break;
                                case 69:
                                    {
                                    int LA150_55 = input.LA(3);

                                    if ( (synpred237()) ) {
                                        alt150=1;
                                    }
                                    }
                                    break;
                                case 65:
                                    {
                                    int LA150_56 = input.LA(3);

                                    if ( (synpred237()) ) {
                                        alt150=1;
                                    }
                                    }
                                    break;
                                case 37:
                                    {
                                    alt150=1;
                                    }
                                    break;
                                case 40:
                                    {
                                    int LA150_58 = input.LA(3);

                                    if ( (synpred237()) ) {
                                        alt150=1;
                                    }
                                    }
                                    break;
                            }

                            }
                            break;
                    }

                    switch (alt150) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:949:35: identifierSuffix
                            {
                            pushFollow(FOLLOW_identifierSuffix_in_primary5205);
                            identifierSuffix();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:950:9: t1= 'super' superSuffix
                    {
                    t1=(Token)input.LT(1);
                    match(input,65,FOLLOW_65_in_primary5219); if (failed) return ;
                    pushFollow(FOLLOW_superSuffix_in_primary5221);
                    superSuffix();
                    _fsp--;
                    if (failed) return ;
                    if ( backtracking==0 ) {
                       addSuperMethodInvocation(t1.getLine()); 
                    }

                    }
                    break;
                case 4 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:951:9: literal
                    {
                    pushFollow(FOLLOW_literal_in_primary5233);
                    literal();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 5 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:952:9: 'new' creator
                    {
                    match(input,113,FOLLOW_113_in_primary5243); if (failed) return ;
                    pushFollow(FOLLOW_creator_in_primary5245);
                    creator();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 6 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:953:9: Identifier ( '.' Identifier )* ( identifierSuffix )?
                    {
                    match(input,Identifier,FOLLOW_Identifier_in_primary5255); if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:953:20: ( '.' Identifier )*
                    loop151:
                    do {
                        int alt151=2;
                        int LA151_0 = input.LA(1);

                        if ( (LA151_0==29) ) {
                            int LA151_3 = input.LA(2);

                            if ( (LA151_3==Identifier) ) {
                                int LA151_34 = input.LA(3);

                                if ( (synpred242()) ) {
                                    alt151=1;
                                }


                            }


                        }


                        switch (alt151) {
                    	case 1 :
                    	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:953:21: '.' Identifier
                    	    {
                    	    match(input,29,FOLLOW_29_in_primary5258); if (failed) return ;
                    	    match(input,Identifier,FOLLOW_Identifier_in_primary5260); if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop151;
                        }
                    } while (true);

                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:953:38: ( identifierSuffix )?
                    int alt152=2;
                    switch ( input.LA(1) ) {
                        case 48:
                            {
                            switch ( input.LA(2) ) {
                                case 49:
                                    {
                                    alt152=1;
                                    }
                                    break;
                                case 105:
                                    {
                                    int LA152_34 = input.LA(3);

                                    if ( (synpred243()) ) {
                                        alt152=1;
                                    }
                                    }
                                    break;
                                case 106:
                                    {
                                    int LA152_35 = input.LA(3);

                                    if ( (synpred243()) ) {
                                        alt152=1;
                                    }
                                    }
                                    break;
                                case 109:
                                    {
                                    int LA152_36 = input.LA(3);

                                    if ( (synpred243()) ) {
                                        alt152=1;
                                    }
                                    }
                                    break;
                                case 110:
                                    {
                                    int LA152_37 = input.LA(3);

                                    if ( (synpred243()) ) {
                                        alt152=1;
                                    }
                                    }
                                    break;
                                case 111:
                                    {
                                    int LA152_38 = input.LA(3);

                                    if ( (synpred243()) ) {
                                        alt152=1;
                                    }
                                    }
                                    break;
                                case 112:
                                    {
                                    int LA152_39 = input.LA(3);

                                    if ( (synpred243()) ) {
                                        alt152=1;
                                    }
                                    }
                                    break;
                                case 66:
                                    {
                                    int LA152_40 = input.LA(3);

                                    if ( (synpred243()) ) {
                                        alt152=1;
                                    }
                                    }
                                    break;
                                case 69:
                                    {
                                    int LA152_41 = input.LA(3);

                                    if ( (synpred243()) ) {
                                        alt152=1;
                                    }
                                    }
                                    break;
                                case 65:
                                    {
                                    int LA152_42 = input.LA(3);

                                    if ( (synpred243()) ) {
                                        alt152=1;
                                    }
                                    }
                                    break;
                                case HexLiteral:
                                case OctalLiteral:
                                case DecimalLiteral:
                                    {
                                    int LA152_43 = input.LA(3);

                                    if ( (synpred243()) ) {
                                        alt152=1;
                                    }
                                    }
                                    break;
                                case FloatingPointLiteral:
                                    {
                                    int LA152_44 = input.LA(3);

                                    if ( (synpred243()) ) {
                                        alt152=1;
                                    }
                                    }
                                    break;
                                case CharacterLiteral:
                                    {
                                    int LA152_45 = input.LA(3);

                                    if ( (synpred243()) ) {
                                        alt152=1;
                                    }
                                    }
                                    break;
                                case StringLiteral:
                                    {
                                    int LA152_46 = input.LA(3);

                                    if ( (synpred243()) ) {
                                        alt152=1;
                                    }
                                    }
                                    break;
                                case 71:
                                case 72:
                                    {
                                    int LA152_47 = input.LA(3);

                                    if ( (synpred243()) ) {
                                        alt152=1;
                                    }
                                    }
                                    break;
                                case 70:
                                    {
                                    int LA152_48 = input.LA(3);

                                    if ( (synpred243()) ) {
                                        alt152=1;
                                    }
                                    }
                                    break;
                                case 113:
                                    {
                                    int LA152_49 = input.LA(3);

                                    if ( (synpred243()) ) {
                                        alt152=1;
                                    }
                                    }
                                    break;
                                case Identifier:
                                    {
                                    int LA152_50 = input.LA(3);

                                    if ( (synpred243()) ) {
                                        alt152=1;
                                    }
                                    }
                                    break;
                                case 56:
                                case 57:
                                case 58:
                                case 59:
                                case 60:
                                case 61:
                                case 62:
                                case 63:
                                    {
                                    int LA152_51 = input.LA(3);

                                    if ( (synpred243()) ) {
                                        alt152=1;
                                    }
                                    }
                                    break;
                                case 47:
                                    {
                                    int LA152_52 = input.LA(3);

                                    if ( (synpred243()) ) {
                                        alt152=1;
                                    }
                                    }
                                    break;
                            }

                            }
                            break;
                        case 66:
                            {
                            alt152=1;
                            }
                            break;
                        case 29:
                            {
                            switch ( input.LA(2) ) {
                                case 113:
                                    {
                                    int LA152_53 = input.LA(3);

                                    if ( (synpred243()) ) {
                                        alt152=1;
                                    }
                                    }
                                    break;
                                case 69:
                                    {
                                    int LA152_55 = input.LA(3);

                                    if ( (synpred243()) ) {
                                        alt152=1;
                                    }
                                    }
                                    break;
                                case 65:
                                    {
                                    int LA152_56 = input.LA(3);

                                    if ( (synpred243()) ) {
                                        alt152=1;
                                    }
                                    }
                                    break;
                                case 37:
                                    {
                                    alt152=1;
                                    }
                                    break;
                                case 40:
                                    {
                                    int LA152_58 = input.LA(3);

                                    if ( (synpred243()) ) {
                                        alt152=1;
                                    }
                                    }
                                    break;
                            }

                            }
                            break;
                    }

                    switch (alt152) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:953:39: identifierSuffix
                            {
                            pushFollow(FOLLOW_identifierSuffix_in_primary5265);
                            identifierSuffix();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 7 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:954:9: primitiveType ( '[' ']' )* '.' 'class'
                    {
                    pushFollow(FOLLOW_primitiveType_in_primary5277);
                    primitiveType();
                    _fsp--;
                    if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:954:23: ( '[' ']' )*
                    loop153:
                    do {
                        int alt153=2;
                        int LA153_0 = input.LA(1);

                        if ( (LA153_0==48) ) {
                            alt153=1;
                        }


                        switch (alt153) {
                    	case 1 :
                    	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:954:24: '[' ']'
                    	    {
                    	    match(input,48,FOLLOW_48_in_primary5280); if (failed) return ;
                    	    match(input,49,FOLLOW_49_in_primary5282); if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop153;
                        }
                    } while (true);

                    match(input,29,FOLLOW_29_in_primary5286); if (failed) return ;
                    match(input,37,FOLLOW_37_in_primary5288); if (failed) return ;

                    }
                    break;
                case 8 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:955:9: 'void' '.' 'class'
                    {
                    match(input,47,FOLLOW_47_in_primary5298); if (failed) return ;
                    match(input,29,FOLLOW_29_in_primary5300); if (failed) return ;
                    match(input,37,FOLLOW_37_in_primary5302); if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 124, primary_StartIndex); }
        }
        return ;
    }
    // $ANTLR end primary


    // $ANTLR start identifierSuffix
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:958:1: identifierSuffix : ( ( '[' ']' )+ '.' 'class' | ( '[' expression ']' )+ | arguments | '.' 'class' | '.' explicitGenericInvocation | '.' 'this' | '.' 'super' arguments | '.' 'new' innerCreator );
    public final void identifierSuffix() throws RecognitionException {
        int identifierSuffix_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 125) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:959:5: ( ( '[' ']' )+ '.' 'class' | ( '[' expression ']' )+ | arguments | '.' 'class' | '.' explicitGenericInvocation | '.' 'this' | '.' 'super' arguments | '.' 'new' innerCreator )
            int alt157=8;
            switch ( input.LA(1) ) {
            case 48:
                {
                int LA157_1 = input.LA(2);

                if ( (LA157_1==49) ) {
                    alt157=1;
                }
                else if ( (LA157_1==Identifier||(LA157_1>=FloatingPointLiteral && LA157_1<=DecimalLiteral)||LA157_1==47||(LA157_1>=56 && LA157_1<=63)||(LA157_1>=65 && LA157_1<=66)||(LA157_1>=69 && LA157_1<=72)||(LA157_1>=105 && LA157_1<=106)||(LA157_1>=109 && LA157_1<=113)) ) {
                    alt157=2;
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("958:1: identifierSuffix : ( ( '[' ']' )+ '.' 'class' | ( '[' expression ']' )+ | arguments | '.' 'class' | '.' explicitGenericInvocation | '.' 'this' | '.' 'super' arguments | '.' 'new' innerCreator );", 157, 1, input);

                    throw nvae;
                }
                }
                break;
            case 66:
                {
                alt157=3;
                }
                break;
            case 29:
                {
                switch ( input.LA(2) ) {
                case 65:
                    {
                    alt157=7;
                    }
                    break;
                case 69:
                    {
                    alt157=6;
                    }
                    break;
                case 113:
                    {
                    alt157=8;
                    }
                    break;
                case 37:
                    {
                    alt157=4;
                    }
                    break;
                case 40:
                    {
                    alt157=5;
                    }
                    break;
                default:
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("958:1: identifierSuffix : ( ( '[' ']' )+ '.' 'class' | ( '[' expression ']' )+ | arguments | '.' 'class' | '.' explicitGenericInvocation | '.' 'this' | '.' 'super' arguments | '.' 'new' innerCreator );", 157, 3, input);

                    throw nvae;
                }

                }
                break;
            default:
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("958:1: identifierSuffix : ( ( '[' ']' )+ '.' 'class' | ( '[' expression ']' )+ | arguments | '.' 'class' | '.' explicitGenericInvocation | '.' 'this' | '.' 'super' arguments | '.' 'new' innerCreator );", 157, 0, input);

                throw nvae;
            }

            switch (alt157) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:959:9: ( '[' ']' )+ '.' 'class'
                    {
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:959:9: ( '[' ']' )+
                    int cnt155=0;
                    loop155:
                    do {
                        int alt155=2;
                        int LA155_0 = input.LA(1);

                        if ( (LA155_0==48) ) {
                            alt155=1;
                        }


                        switch (alt155) {
                    	case 1 :
                    	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:959:10: '[' ']'
                    	    {
                    	    match(input,48,FOLLOW_48_in_identifierSuffix5322); if (failed) return ;
                    	    match(input,49,FOLLOW_49_in_identifierSuffix5324); if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt155 >= 1 ) break loop155;
                    	    if (backtracking>0) {failed=true; return ;}
                                EarlyExitException eee =
                                    new EarlyExitException(155, input);
                                throw eee;
                        }
                        cnt155++;
                    } while (true);

                    match(input,29,FOLLOW_29_in_identifierSuffix5328); if (failed) return ;
                    match(input,37,FOLLOW_37_in_identifierSuffix5330); if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:960:9: ( '[' expression ']' )+
                    {
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:960:9: ( '[' expression ']' )+
                    int cnt156=0;
                    loop156:
                    do {
                        int alt156=2;
                        int LA156_0 = input.LA(1);

                        if ( (LA156_0==48) ) {
                            switch ( input.LA(2) ) {
                            case 105:
                                {
                                int LA156_32 = input.LA(3);

                                if ( (synpred249()) ) {
                                    alt156=1;
                                }


                                }
                                break;
                            case 106:
                                {
                                int LA156_33 = input.LA(3);

                                if ( (synpred249()) ) {
                                    alt156=1;
                                }


                                }
                                break;
                            case 109:
                                {
                                int LA156_34 = input.LA(3);

                                if ( (synpred249()) ) {
                                    alt156=1;
                                }


                                }
                                break;
                            case 110:
                                {
                                int LA156_35 = input.LA(3);

                                if ( (synpred249()) ) {
                                    alt156=1;
                                }


                                }
                                break;
                            case 111:
                                {
                                int LA156_36 = input.LA(3);

                                if ( (synpred249()) ) {
                                    alt156=1;
                                }


                                }
                                break;
                            case 112:
                                {
                                int LA156_37 = input.LA(3);

                                if ( (synpred249()) ) {
                                    alt156=1;
                                }


                                }
                                break;
                            case 66:
                                {
                                int LA156_38 = input.LA(3);

                                if ( (synpred249()) ) {
                                    alt156=1;
                                }


                                }
                                break;
                            case 69:
                                {
                                int LA156_39 = input.LA(3);

                                if ( (synpred249()) ) {
                                    alt156=1;
                                }


                                }
                                break;
                            case 65:
                                {
                                int LA156_40 = input.LA(3);

                                if ( (synpred249()) ) {
                                    alt156=1;
                                }


                                }
                                break;
                            case HexLiteral:
                            case OctalLiteral:
                            case DecimalLiteral:
                                {
                                int LA156_41 = input.LA(3);

                                if ( (synpred249()) ) {
                                    alt156=1;
                                }


                                }
                                break;
                            case FloatingPointLiteral:
                                {
                                int LA156_42 = input.LA(3);

                                if ( (synpred249()) ) {
                                    alt156=1;
                                }


                                }
                                break;
                            case CharacterLiteral:
                                {
                                int LA156_43 = input.LA(3);

                                if ( (synpred249()) ) {
                                    alt156=1;
                                }


                                }
                                break;
                            case StringLiteral:
                                {
                                int LA156_44 = input.LA(3);

                                if ( (synpred249()) ) {
                                    alt156=1;
                                }


                                }
                                break;
                            case 71:
                            case 72:
                                {
                                int LA156_45 = input.LA(3);

                                if ( (synpred249()) ) {
                                    alt156=1;
                                }


                                }
                                break;
                            case 70:
                                {
                                int LA156_46 = input.LA(3);

                                if ( (synpred249()) ) {
                                    alt156=1;
                                }


                                }
                                break;
                            case 113:
                                {
                                int LA156_47 = input.LA(3);

                                if ( (synpred249()) ) {
                                    alt156=1;
                                }


                                }
                                break;
                            case Identifier:
                                {
                                int LA156_48 = input.LA(3);

                                if ( (synpred249()) ) {
                                    alt156=1;
                                }


                                }
                                break;
                            case 56:
                            case 57:
                            case 58:
                            case 59:
                            case 60:
                            case 61:
                            case 62:
                            case 63:
                                {
                                int LA156_49 = input.LA(3);

                                if ( (synpred249()) ) {
                                    alt156=1;
                                }


                                }
                                break;
                            case 47:
                                {
                                int LA156_50 = input.LA(3);

                                if ( (synpred249()) ) {
                                    alt156=1;
                                }


                                }
                                break;

                            }

                        }


                        switch (alt156) {
                    	case 1 :
                    	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:960:10: '[' expression ']'
                    	    {
                    	    match(input,48,FOLLOW_48_in_identifierSuffix5341); if (failed) return ;
                    	    pushFollow(FOLLOW_expression_in_identifierSuffix5343);
                    	    expression();
                    	    _fsp--;
                    	    if (failed) return ;
                    	    match(input,49,FOLLOW_49_in_identifierSuffix5345); if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt156 >= 1 ) break loop156;
                    	    if (backtracking>0) {failed=true; return ;}
                                EarlyExitException eee =
                                    new EarlyExitException(156, input);
                                throw eee;
                        }
                        cnt156++;
                    } while (true);


                    }
                    break;
                case 3 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:961:9: arguments
                    {
                    pushFollow(FOLLOW_arguments_in_identifierSuffix5358);
                    arguments();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 4 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:962:9: '.' 'class'
                    {
                    match(input,29,FOLLOW_29_in_identifierSuffix5368); if (failed) return ;
                    match(input,37,FOLLOW_37_in_identifierSuffix5370); if (failed) return ;

                    }
                    break;
                case 5 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:963:9: '.' explicitGenericInvocation
                    {
                    match(input,29,FOLLOW_29_in_identifierSuffix5380); if (failed) return ;
                    pushFollow(FOLLOW_explicitGenericInvocation_in_identifierSuffix5382);
                    explicitGenericInvocation();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 6 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:964:9: '.' 'this'
                    {
                    match(input,29,FOLLOW_29_in_identifierSuffix5392); if (failed) return ;
                    match(input,69,FOLLOW_69_in_identifierSuffix5394); if (failed) return ;

                    }
                    break;
                case 7 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:965:9: '.' 'super' arguments
                    {
                    match(input,29,FOLLOW_29_in_identifierSuffix5404); if (failed) return ;
                    match(input,65,FOLLOW_65_in_identifierSuffix5406); if (failed) return ;
                    pushFollow(FOLLOW_arguments_in_identifierSuffix5408);
                    arguments();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 8 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:966:9: '.' 'new' innerCreator
                    {
                    match(input,29,FOLLOW_29_in_identifierSuffix5418); if (failed) return ;
                    match(input,113,FOLLOW_113_in_identifierSuffix5420); if (failed) return ;
                    pushFollow(FOLLOW_innerCreator_in_identifierSuffix5422);
                    innerCreator();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 125, identifierSuffix_StartIndex); }
        }
        return ;
    }
    // $ANTLR end identifierSuffix


    // $ANTLR start creator
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:969:1: creator : ( nonWildcardTypeArguments createdName classCreatorRest | createdName ( arrayCreatorRest | classCreatorRest ) );
    public final void creator() throws RecognitionException {
        int creator_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 126) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:970:5: ( nonWildcardTypeArguments createdName classCreatorRest | createdName ( arrayCreatorRest | classCreatorRest ) )
            int alt159=2;
            int LA159_0 = input.LA(1);

            if ( (LA159_0==40) ) {
                alt159=1;
            }
            else if ( (LA159_0==Identifier||(LA159_0>=56 && LA159_0<=63)) ) {
                alt159=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("969:1: creator : ( nonWildcardTypeArguments createdName classCreatorRest | createdName ( arrayCreatorRest | classCreatorRest ) );", 159, 0, input);

                throw nvae;
            }
            switch (alt159) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:970:9: nonWildcardTypeArguments createdName classCreatorRest
                    {
                    pushFollow(FOLLOW_nonWildcardTypeArguments_in_creator5441);
                    nonWildcardTypeArguments();
                    _fsp--;
                    if (failed) return ;
                    pushFollow(FOLLOW_createdName_in_creator5443);
                    createdName();
                    _fsp--;
                    if (failed) return ;
                    pushFollow(FOLLOW_classCreatorRest_in_creator5445);
                    classCreatorRest();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:971:9: createdName ( arrayCreatorRest | classCreatorRest )
                    {
                    pushFollow(FOLLOW_createdName_in_creator5455);
                    createdName();
                    _fsp--;
                    if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:971:21: ( arrayCreatorRest | classCreatorRest )
                    int alt158=2;
                    int LA158_0 = input.LA(1);

                    if ( (LA158_0==48) ) {
                        alt158=1;
                    }
                    else if ( (LA158_0==66) ) {
                        alt158=2;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("971:21: ( arrayCreatorRest | classCreatorRest )", 158, 0, input);

                        throw nvae;
                    }
                    switch (alt158) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:971:22: arrayCreatorRest
                            {
                            pushFollow(FOLLOW_arrayCreatorRest_in_creator5458);
                            arrayCreatorRest();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;
                        case 2 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:971:41: classCreatorRest
                            {
                            pushFollow(FOLLOW_classCreatorRest_in_creator5462);
                            classCreatorRest();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 126, creator_StartIndex); }
        }
        return ;
    }
    // $ANTLR end creator


    // $ANTLR start createdName
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:974:1: createdName : ( classOrInterfaceType | primitiveType );
    public final void createdName() throws RecognitionException {
        int createdName_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 127) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:975:5: ( classOrInterfaceType | primitiveType )
            int alt160=2;
            int LA160_0 = input.LA(1);

            if ( (LA160_0==Identifier) ) {
                alt160=1;
            }
            else if ( ((LA160_0>=56 && LA160_0<=63)) ) {
                alt160=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("974:1: createdName : ( classOrInterfaceType | primitiveType );", 160, 0, input);

                throw nvae;
            }
            switch (alt160) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:975:9: classOrInterfaceType
                    {
                    pushFollow(FOLLOW_classOrInterfaceType_in_createdName5482);
                    classOrInterfaceType();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:976:9: primitiveType
                    {
                    pushFollow(FOLLOW_primitiveType_in_createdName5492);
                    primitiveType();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 127, createdName_StartIndex); }
        }
        return ;
    }
    // $ANTLR end createdName


    // $ANTLR start innerCreator
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:979:1: innerCreator : ( nonWildcardTypeArguments )? Identifier classCreatorRest ;
    public final void innerCreator() throws RecognitionException {
        int innerCreator_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 128) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:980:5: ( ( nonWildcardTypeArguments )? Identifier classCreatorRest )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:980:9: ( nonWildcardTypeArguments )? Identifier classCreatorRest
            {
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:980:9: ( nonWildcardTypeArguments )?
            int alt161=2;
            int LA161_0 = input.LA(1);

            if ( (LA161_0==40) ) {
                alt161=1;
            }
            switch (alt161) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:980:10: nonWildcardTypeArguments
                    {
                    pushFollow(FOLLOW_nonWildcardTypeArguments_in_innerCreator5516);
                    nonWildcardTypeArguments();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            match(input,Identifier,FOLLOW_Identifier_in_innerCreator5520); if (failed) return ;
            pushFollow(FOLLOW_classCreatorRest_in_innerCreator5522);
            classCreatorRest();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 128, innerCreator_StartIndex); }
        }
        return ;
    }
    // $ANTLR end innerCreator


    // $ANTLR start arrayCreatorRest
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:983:1: arrayCreatorRest : '[' ( ']' ( '[' ']' )* arrayInitializer | expression ']' ( '[' expression ']' )* ( '[' ']' )* ) ;
    public final void arrayCreatorRest() throws RecognitionException {
        int arrayCreatorRest_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 129) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:984:5: ( '[' ( ']' ( '[' ']' )* arrayInitializer | expression ']' ( '[' expression ']' )* ( '[' ']' )* ) )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:984:9: '[' ( ']' ( '[' ']' )* arrayInitializer | expression ']' ( '[' expression ']' )* ( '[' ']' )* )
            {
            match(input,48,FOLLOW_48_in_arrayCreatorRest5541); if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:985:9: ( ']' ( '[' ']' )* arrayInitializer | expression ']' ( '[' expression ']' )* ( '[' ']' )* )
            int alt165=2;
            int LA165_0 = input.LA(1);

            if ( (LA165_0==49) ) {
                alt165=1;
            }
            else if ( (LA165_0==Identifier||(LA165_0>=FloatingPointLiteral && LA165_0<=DecimalLiteral)||LA165_0==47||(LA165_0>=56 && LA165_0<=63)||(LA165_0>=65 && LA165_0<=66)||(LA165_0>=69 && LA165_0<=72)||(LA165_0>=105 && LA165_0<=106)||(LA165_0>=109 && LA165_0<=113)) ) {
                alt165=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("985:9: ( ']' ( '[' ']' )* arrayInitializer | expression ']' ( '[' expression ']' )* ( '[' ']' )* )", 165, 0, input);

                throw nvae;
            }
            switch (alt165) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:985:13: ']' ( '[' ']' )* arrayInitializer
                    {
                    match(input,49,FOLLOW_49_in_arrayCreatorRest5555); if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:985:17: ( '[' ']' )*
                    loop162:
                    do {
                        int alt162=2;
                        int LA162_0 = input.LA(1);

                        if ( (LA162_0==48) ) {
                            alt162=1;
                        }


                        switch (alt162) {
                    	case 1 :
                    	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:985:18: '[' ']'
                    	    {
                    	    match(input,48,FOLLOW_48_in_arrayCreatorRest5558); if (failed) return ;
                    	    match(input,49,FOLLOW_49_in_arrayCreatorRest5560); if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop162;
                        }
                    } while (true);

                    pushFollow(FOLLOW_arrayInitializer_in_arrayCreatorRest5564);
                    arrayInitializer();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:986:13: expression ']' ( '[' expression ']' )* ( '[' ']' )*
                    {
                    pushFollow(FOLLOW_expression_in_arrayCreatorRest5578);
                    expression();
                    _fsp--;
                    if (failed) return ;
                    match(input,49,FOLLOW_49_in_arrayCreatorRest5580); if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:986:28: ( '[' expression ']' )*
                    loop163:
                    do {
                        int alt163=2;
                        int LA163_0 = input.LA(1);

                        if ( (LA163_0==48) ) {
                            switch ( input.LA(2) ) {
                            case 105:
                                {
                                int LA163_33 = input.LA(3);

                                if ( (synpred262()) ) {
                                    alt163=1;
                                }


                                }
                                break;
                            case 106:
                                {
                                int LA163_34 = input.LA(3);

                                if ( (synpred262()) ) {
                                    alt163=1;
                                }


                                }
                                break;
                            case 109:
                                {
                                int LA163_35 = input.LA(3);

                                if ( (synpred262()) ) {
                                    alt163=1;
                                }


                                }
                                break;
                            case 110:
                                {
                                int LA163_36 = input.LA(3);

                                if ( (synpred262()) ) {
                                    alt163=1;
                                }


                                }
                                break;
                            case 111:
                                {
                                int LA163_37 = input.LA(3);

                                if ( (synpred262()) ) {
                                    alt163=1;
                                }


                                }
                                break;
                            case 112:
                                {
                                int LA163_38 = input.LA(3);

                                if ( (synpred262()) ) {
                                    alt163=1;
                                }


                                }
                                break;
                            case 66:
                                {
                                int LA163_39 = input.LA(3);

                                if ( (synpred262()) ) {
                                    alt163=1;
                                }


                                }
                                break;
                            case 69:
                                {
                                int LA163_40 = input.LA(3);

                                if ( (synpred262()) ) {
                                    alt163=1;
                                }


                                }
                                break;
                            case 65:
                                {
                                int LA163_41 = input.LA(3);

                                if ( (synpred262()) ) {
                                    alt163=1;
                                }


                                }
                                break;
                            case HexLiteral:
                            case OctalLiteral:
                            case DecimalLiteral:
                                {
                                int LA163_42 = input.LA(3);

                                if ( (synpred262()) ) {
                                    alt163=1;
                                }


                                }
                                break;
                            case FloatingPointLiteral:
                                {
                                int LA163_43 = input.LA(3);

                                if ( (synpred262()) ) {
                                    alt163=1;
                                }


                                }
                                break;
                            case CharacterLiteral:
                                {
                                int LA163_44 = input.LA(3);

                                if ( (synpred262()) ) {
                                    alt163=1;
                                }


                                }
                                break;
                            case StringLiteral:
                                {
                                int LA163_45 = input.LA(3);

                                if ( (synpred262()) ) {
                                    alt163=1;
                                }


                                }
                                break;
                            case 71:
                            case 72:
                                {
                                int LA163_46 = input.LA(3);

                                if ( (synpred262()) ) {
                                    alt163=1;
                                }


                                }
                                break;
                            case 70:
                                {
                                int LA163_47 = input.LA(3);

                                if ( (synpred262()) ) {
                                    alt163=1;
                                }


                                }
                                break;
                            case 113:
                                {
                                int LA163_48 = input.LA(3);

                                if ( (synpred262()) ) {
                                    alt163=1;
                                }


                                }
                                break;
                            case Identifier:
                                {
                                int LA163_49 = input.LA(3);

                                if ( (synpred262()) ) {
                                    alt163=1;
                                }


                                }
                                break;
                            case 56:
                            case 57:
                            case 58:
                            case 59:
                            case 60:
                            case 61:
                            case 62:
                            case 63:
                                {
                                int LA163_50 = input.LA(3);

                                if ( (synpred262()) ) {
                                    alt163=1;
                                }


                                }
                                break;
                            case 47:
                                {
                                int LA163_51 = input.LA(3);

                                if ( (synpred262()) ) {
                                    alt163=1;
                                }


                                }
                                break;

                            }

                        }


                        switch (alt163) {
                    	case 1 :
                    	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:986:29: '[' expression ']'
                    	    {
                    	    match(input,48,FOLLOW_48_in_arrayCreatorRest5583); if (failed) return ;
                    	    pushFollow(FOLLOW_expression_in_arrayCreatorRest5585);
                    	    expression();
                    	    _fsp--;
                    	    if (failed) return ;
                    	    match(input,49,FOLLOW_49_in_arrayCreatorRest5587); if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop163;
                        }
                    } while (true);

                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:986:50: ( '[' ']' )*
                    loop164:
                    do {
                        int alt164=2;
                        int LA164_0 = input.LA(1);

                        if ( (LA164_0==48) ) {
                            int LA164_2 = input.LA(2);

                            if ( (LA164_2==49) ) {
                                alt164=1;
                            }


                        }


                        switch (alt164) {
                    	case 1 :
                    	    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:986:51: '[' ']'
                    	    {
                    	    match(input,48,FOLLOW_48_in_arrayCreatorRest5592); if (failed) return ;
                    	    match(input,49,FOLLOW_49_in_arrayCreatorRest5594); if (failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop164;
                        }
                    } while (true);


                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 129, arrayCreatorRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end arrayCreatorRest


    // $ANTLR start classCreatorRest
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:990:1: classCreatorRest : arguments ( classBody )? ;
    public final void classCreatorRest() throws RecognitionException {
        int classCreatorRest_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 130) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:991:5: ( arguments ( classBody )? )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:991:9: arguments ( classBody )?
            {
            if ( backtracking==0 ) {
               classDepth++; 
            }
            pushFollow(FOLLOW_arguments_in_classCreatorRest5627);
            arguments();
            _fsp--;
            if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:991:37: ( classBody )?
            int alt166=2;
            int LA166_0 = input.LA(1);

            if ( (LA166_0==44) ) {
                alt166=1;
            }
            switch (alt166) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: classBody
                    {
                    pushFollow(FOLLOW_classBody_in_classCreatorRest5629);
                    classBody();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            if ( backtracking==0 ) {
               classDepth--; 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 130, classCreatorRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end classCreatorRest


    // $ANTLR start explicitGenericInvocation
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:994:1: explicitGenericInvocation : nonWildcardTypeArguments Identifier arguments ;
    public final void explicitGenericInvocation() throws RecognitionException {
        int explicitGenericInvocation_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 131) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:995:5: ( nonWildcardTypeArguments Identifier arguments )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:995:9: nonWildcardTypeArguments Identifier arguments
            {
            pushFollow(FOLLOW_nonWildcardTypeArguments_in_explicitGenericInvocation5655);
            nonWildcardTypeArguments();
            _fsp--;
            if (failed) return ;
            match(input,Identifier,FOLLOW_Identifier_in_explicitGenericInvocation5657); if (failed) return ;
            pushFollow(FOLLOW_arguments_in_explicitGenericInvocation5659);
            arguments();
            _fsp--;
            if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 131, explicitGenericInvocation_StartIndex); }
        }
        return ;
    }
    // $ANTLR end explicitGenericInvocation


    // $ANTLR start nonWildcardTypeArguments
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:998:1: nonWildcardTypeArguments : '<' typeList '>' ;
    public final void nonWildcardTypeArguments() throws RecognitionException {
        int nonWildcardTypeArguments_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 132) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:999:5: ( '<' typeList '>' )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:999:9: '<' typeList '>'
            {
            match(input,40,FOLLOW_40_in_nonWildcardTypeArguments5682); if (failed) return ;
            pushFollow(FOLLOW_typeList_in_nonWildcardTypeArguments5684);
            typeList();
            _fsp--;
            if (failed) return ;
            match(input,42,FOLLOW_42_in_nonWildcardTypeArguments5686); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 132, nonWildcardTypeArguments_StartIndex); }
        }
        return ;
    }
    // $ANTLR end nonWildcardTypeArguments


    // $ANTLR start selector
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:1002:1: selector : ( '.' Identifier ( arguments )? | '.' 'this' | '.' 'super' superSuffix | '.' 'new' innerCreator | '[' expression ']' );
    public final void selector() throws RecognitionException {
        int selector_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 133) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:1003:5: ( '.' Identifier ( arguments )? | '.' 'this' | '.' 'super' superSuffix | '.' 'new' innerCreator | '[' expression ']' )
            int alt168=5;
            int LA168_0 = input.LA(1);

            if ( (LA168_0==29) ) {
                switch ( input.LA(2) ) {
                case 113:
                    {
                    alt168=4;
                    }
                    break;
                case 69:
                    {
                    alt168=2;
                    }
                    break;
                case 65:
                    {
                    alt168=3;
                    }
                    break;
                case Identifier:
                    {
                    alt168=1;
                    }
                    break;
                default:
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("1002:1: selector : ( '.' Identifier ( arguments )? | '.' 'this' | '.' 'super' superSuffix | '.' 'new' innerCreator | '[' expression ']' );", 168, 1, input);

                    throw nvae;
                }

            }
            else if ( (LA168_0==48) ) {
                alt168=5;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("1002:1: selector : ( '.' Identifier ( arguments )? | '.' 'this' | '.' 'super' superSuffix | '.' 'new' innerCreator | '[' expression ']' );", 168, 0, input);

                throw nvae;
            }
            switch (alt168) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:1003:9: '.' Identifier ( arguments )?
                    {
                    match(input,29,FOLLOW_29_in_selector5709); if (failed) return ;
                    match(input,Identifier,FOLLOW_Identifier_in_selector5711); if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:1003:24: ( arguments )?
                    int alt167=2;
                    int LA167_0 = input.LA(1);

                    if ( (LA167_0==66) ) {
                        alt167=1;
                    }
                    switch (alt167) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:1003:25: arguments
                            {
                            pushFollow(FOLLOW_arguments_in_selector5714);
                            arguments();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:1004:9: '.' 'this'
                    {
                    match(input,29,FOLLOW_29_in_selector5726); if (failed) return ;
                    match(input,69,FOLLOW_69_in_selector5728); if (failed) return ;

                    }
                    break;
                case 3 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:1005:9: '.' 'super' superSuffix
                    {
                    match(input,29,FOLLOW_29_in_selector5738); if (failed) return ;
                    match(input,65,FOLLOW_65_in_selector5740); if (failed) return ;
                    pushFollow(FOLLOW_superSuffix_in_selector5742);
                    superSuffix();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 4 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:1006:9: '.' 'new' innerCreator
                    {
                    match(input,29,FOLLOW_29_in_selector5752); if (failed) return ;
                    match(input,113,FOLLOW_113_in_selector5754); if (failed) return ;
                    pushFollow(FOLLOW_innerCreator_in_selector5756);
                    innerCreator();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 5 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:1007:9: '[' expression ']'
                    {
                    match(input,48,FOLLOW_48_in_selector5766); if (failed) return ;
                    pushFollow(FOLLOW_expression_in_selector5768);
                    expression();
                    _fsp--;
                    if (failed) return ;
                    match(input,49,FOLLOW_49_in_selector5770); if (failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 133, selector_StartIndex); }
        }
        return ;
    }
    // $ANTLR end selector


    // $ANTLR start superSuffix
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:1010:1: superSuffix : ( arguments | '.' Identifier ( arguments )? );
    public final void superSuffix() throws RecognitionException {
        int superSuffix_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 134) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:1011:5: ( arguments | '.' Identifier ( arguments )? )
            int alt170=2;
            int LA170_0 = input.LA(1);

            if ( (LA170_0==66) ) {
                alt170=1;
            }
            else if ( (LA170_0==29) ) {
                alt170=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("1010:1: superSuffix : ( arguments | '.' Identifier ( arguments )? );", 170, 0, input);

                throw nvae;
            }
            switch (alt170) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:1011:9: arguments
                    {
                    pushFollow(FOLLOW_arguments_in_superSuffix5793);
                    arguments();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;
                case 2 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:1012:9: '.' Identifier ( arguments )?
                    {
                    match(input,29,FOLLOW_29_in_superSuffix5803); if (failed) return ;
                    match(input,Identifier,FOLLOW_Identifier_in_superSuffix5805); if (failed) return ;
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:1012:24: ( arguments )?
                    int alt169=2;
                    int LA169_0 = input.LA(1);

                    if ( (LA169_0==66) ) {
                        alt169=1;
                    }
                    switch (alt169) {
                        case 1 :
                            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:1012:25: arguments
                            {
                            pushFollow(FOLLOW_arguments_in_superSuffix5808);
                            arguments();
                            _fsp--;
                            if (failed) return ;

                            }
                            break;

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 134, superSuffix_StartIndex); }
        }
        return ;
    }
    // $ANTLR end superSuffix


    // $ANTLR start arguments
    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:1015:1: arguments : '(' ( expressionList )? ')' ;
    public final void arguments() throws RecognitionException {
        int arguments_StartIndex = input.index();
        try {
            if ( backtracking>0 && alreadyParsedRule(input, 135) ) { return ; }
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:1016:5: ( '(' ( expressionList )? ')' )
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:1016:9: '(' ( expressionList )? ')'
            {
            match(input,66,FOLLOW_66_in_arguments5829); if (failed) return ;
            // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:1016:13: ( expressionList )?
            int alt171=2;
            int LA171_0 = input.LA(1);

            if ( (LA171_0==Identifier||(LA171_0>=FloatingPointLiteral && LA171_0<=DecimalLiteral)||LA171_0==47||(LA171_0>=56 && LA171_0<=63)||(LA171_0>=65 && LA171_0<=66)||(LA171_0>=69 && LA171_0<=72)||(LA171_0>=105 && LA171_0<=106)||(LA171_0>=109 && LA171_0<=113)) ) {
                alt171=1;
            }
            switch (alt171) {
                case 1 :
                    // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:0:0: expressionList
                    {
                    pushFollow(FOLLOW_expressionList_in_arguments5831);
                    expressionList();
                    _fsp--;
                    if (failed) return ;

                    }
                    break;

            }

            match(input,67,FOLLOW_67_in_arguments5834); if (failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( backtracking>0 ) { memoize(input, 135, arguments_StartIndex); }
        }
        return ;
    }
    // $ANTLR end arguments

    // $ANTLR start synpred1
    public final void synpred1_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:275:9: ( '@' )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:275:10: '@'
        {
        match(input,73,FOLLOW_73_in_synpred168); if (failed) return ;

        }
    }
    // $ANTLR end synpred1

    // $ANTLR start synpred113
    public final void synpred113_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:597:13: ( explicitConstructorInvocation )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:597:13: explicitConstructorInvocation
        {
        pushFollow(FOLLOW_explicitConstructorInvocation_in_synpred1132528);
        explicitConstructorInvocation();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred113

    // $ANTLR start synpred117
    public final void synpred117_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:601:9: ( ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';' )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:601:9: ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';'
        {
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:601:9: ( nonWildcardTypeArguments )?
        int alt181=2;
        int LA181_0 = input.LA(1);

        if ( (LA181_0==40) ) {
            alt181=1;
        }
        switch (alt181) {
            case 1 :
                // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:601:10: nonWildcardTypeArguments
                {
                pushFollow(FOLLOW_nonWildcardTypeArguments_in_synpred1172554);
                nonWildcardTypeArguments();
                _fsp--;
                if (failed) return ;

                }
                break;

        }

        if ( input.LA(1)==65||input.LA(1)==69 ) {
            input.consume();
            errorRecovery=false;failed=false;
        }
        else {
            if (backtracking>0) {failed=true; return ;}
            MismatchedSetException mse =
                new MismatchedSetException(null,input);
            recoverFromMismatchedSet(input,mse,FOLLOW_set_in_synpred1172558);    throw mse;
        }

        pushFollow(FOLLOW_arguments_in_synpred1172570);
        arguments();
        _fsp--;
        if (failed) return ;
        match(input,26,FOLLOW_26_in_synpred1172572); if (failed) return ;

        }
    }
    // $ANTLR end synpred117

    // $ANTLR start synpred128
    public final void synpred128_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:633:9: ( annotation )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:633:9: annotation
        {
        pushFollow(FOLLOW_annotation_in_synpred1282787);
        annotation();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred128

    // $ANTLR start synpred151
    public final void synpred151_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:713:9: ( localVariableDeclarationStatement )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:713:9: localVariableDeclarationStatement
        {
        pushFollow(FOLLOW_localVariableDeclarationStatement_in_synpred1513323);
        localVariableDeclarationStatement();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred151

    // $ANTLR start synpred152
    public final void synpred152_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:714:9: ( classOrInterfaceDeclaration )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:714:9: classOrInterfaceDeclaration
        {
        pushFollow(FOLLOW_classOrInterfaceDeclaration_in_synpred1523333);
        classOrInterfaceDeclaration();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred152

    // $ANTLR start synpred157
    public final void synpred157_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:733:54: ( 'else' statement )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:733:54: 'else' statement
        {
        match(input,77,FOLLOW_77_in_synpred1573478); if (failed) return ;
        pushFollow(FOLLOW_statement_in_synpred1573480);
        statement();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred157

    // $ANTLR start synpred162
    public final void synpred162_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:738:11: ( catches 'finally' block )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:738:11: catches 'finally' block
        {
        pushFollow(FOLLOW_catches_in_synpred1623556);
        catches();
        _fsp--;
        if (failed) return ;
        match(input,82,FOLLOW_82_in_synpred1623558); if (failed) return ;
        pushFollow(FOLLOW_block_in_synpred1623560);
        block();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred162

    // $ANTLR start synpred163
    public final void synpred163_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:739:11: ( catches )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:739:11: catches
        {
        pushFollow(FOLLOW_catches_in_synpred1633572);
        catches();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred163

    // $ANTLR start synpred178
    public final void synpred178_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:774:9: ( switchLabel )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:774:9: switchLabel
        {
        pushFollow(FOLLOW_switchLabel_in_synpred1783863);
        switchLabel();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred178

    // $ANTLR start synpred180
    public final void synpred180_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:778:9: ( 'case' constantExpression ':' )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:778:9: 'case' constantExpression ':'
        {
        match(input,89,FOLLOW_89_in_synpred1803890); if (failed) return ;
        pushFollow(FOLLOW_constantExpression_in_synpred1803892);
        constantExpression();
        _fsp--;
        if (failed) return ;
        match(input,75,FOLLOW_75_in_synpred1803894); if (failed) return ;

        }
    }
    // $ANTLR end synpred180

    // $ANTLR start synpred181
    public final void synpred181_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:779:9: ( 'case' enumConstantName ':' )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:779:9: 'case' enumConstantName ':'
        {
        match(input,89,FOLLOW_89_in_synpred1813904); if (failed) return ;
        pushFollow(FOLLOW_enumConstantName_in_synpred1813906);
        enumConstantName();
        _fsp--;
        if (failed) return ;
        match(input,75,FOLLOW_75_in_synpred1813908); if (failed) return ;

        }
    }
    // $ANTLR end synpred181

    // $ANTLR start synpred182
    public final void synpred182_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:785:9: ( enhancedForControl )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:785:9: enhancedForControl
        {
        pushFollow(FOLLOW_enhancedForControl_in_synpred1823951);
        enhancedForControl();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred182

    // $ANTLR start synpred186
    public final void synpred186_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:790:9: ( localVariableDeclaration )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:790:9: localVariableDeclaration
        {
        pushFollow(FOLLOW_localVariableDeclaration_in_synpred1863991);
        localVariableDeclaration();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred186

    // $ANTLR start synpred188
    public final void synpred188_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:821:32: ( assignmentOperator expression )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:821:32: assignmentOperator expression
        {
        pushFollow(FOLLOW_assignmentOperator_in_synpred1884174);
        assignmentOperator();
        _fsp--;
        if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred1884176);
        expression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred188

    // $ANTLR start synpred198
    public final void synpred198_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:834:9: ( '<' '<' '=' )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:834:10: '<' '<' '='
        {
        match(input,40,FOLLOW_40_in_synpred1984292); if (failed) return ;
        match(input,40,FOLLOW_40_in_synpred1984294); if (failed) return ;
        match(input,51,FOLLOW_51_in_synpred1984296); if (failed) return ;

        }
    }
    // $ANTLR end synpred198

    // $ANTLR start synpred199
    public final void synpred199_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:839:9: ( '>' '>' '>' '=' )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:839:10: '>' '>' '>' '='
        {
        match(input,42,FOLLOW_42_in_synpred1994332); if (failed) return ;
        match(input,42,FOLLOW_42_in_synpred1994334); if (failed) return ;
        match(input,42,FOLLOW_42_in_synpred1994336); if (failed) return ;
        match(input,51,FOLLOW_51_in_synpred1994338); if (failed) return ;

        }
    }
    // $ANTLR end synpred199

    // $ANTLR start synpred200
    public final void synpred200_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:846:9: ( '>' '>' '=' )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:846:10: '>' '>' '='
        {
        match(input,42,FOLLOW_42_in_synpred2004377); if (failed) return ;
        match(input,42,FOLLOW_42_in_synpred2004379); if (failed) return ;
        match(input,51,FOLLOW_51_in_synpred2004381); if (failed) return ;

        }
    }
    // $ANTLR end synpred200

    // $ANTLR start synpred211
    public final void synpred211_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:890:9: ( '<' '=' )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:890:10: '<' '='
        {
        match(input,40,FOLLOW_40_in_synpred2114689); if (failed) return ;
        match(input,51,FOLLOW_51_in_synpred2114691); if (failed) return ;

        }
    }
    // $ANTLR end synpred211

    // $ANTLR start synpred212
    public final void synpred212_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:893:9: ( '>' '=' )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:893:10: '>' '='
        {
        match(input,42,FOLLOW_42_in_synpred2124723); if (failed) return ;
        match(input,51,FOLLOW_51_in_synpred2124725); if (failed) return ;

        }
    }
    // $ANTLR end synpred212

    // $ANTLR start synpred214
    public final void synpred214_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:901:30: ( shiftOp additiveExpression )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:901:30: shiftOp additiveExpression
        {
        pushFollow(FOLLOW_shiftOp_in_synpred2144791);
        shiftOp();
        _fsp--;
        if (failed) return ;
        pushFollow(FOLLOW_additiveExpression_in_synpred2144793);
        additiveExpression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred214

    // $ANTLR start synpred215
    public final void synpred215_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:905:9: ( '<' '<' )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:905:10: '<' '<'
        {
        match(input,40,FOLLOW_40_in_synpred2154816); if (failed) return ;
        match(input,40,FOLLOW_40_in_synpred2154818); if (failed) return ;

        }
    }
    // $ANTLR end synpred215

    // $ANTLR start synpred216
    public final void synpred216_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:908:9: ( '>' '>' '>' )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:908:10: '>' '>' '>'
        {
        match(input,42,FOLLOW_42_in_synpred2164850); if (failed) return ;
        match(input,42,FOLLOW_42_in_synpred2164852); if (failed) return ;
        match(input,42,FOLLOW_42_in_synpred2164854); if (failed) return ;

        }
    }
    // $ANTLR end synpred216

    // $ANTLR start synpred217
    public final void synpred217_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:913:9: ( '>' '>' )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:913:10: '>' '>'
        {
        match(input,42,FOLLOW_42_in_synpred2174890); if (failed) return ;
        match(input,42,FOLLOW_42_in_synpred2174892); if (failed) return ;

        }
    }
    // $ANTLR end synpred217

    // $ANTLR start synpred229
    public final void synpred229_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:938:9: ( castExpression )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:938:9: castExpression
        {
        pushFollow(FOLLOW_castExpression_in_synpred2295101);
        castExpression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred229

    // $ANTLR start synpred233
    public final void synpred233_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:943:8: ( '(' primitiveType ')' unaryExpression )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:943:8: '(' primitiveType ')' unaryExpression
        {
        match(input,66,FOLLOW_66_in_synpred2335139); if (failed) return ;
        pushFollow(FOLLOW_primitiveType_in_synpred2335141);
        primitiveType();
        _fsp--;
        if (failed) return ;
        match(input,67,FOLLOW_67_in_synpred2335143); if (failed) return ;
        pushFollow(FOLLOW_unaryExpression_in_synpred2335145);
        unaryExpression();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred233

    // $ANTLR start synpred234
    public final void synpred234_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:944:13: ( type )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:944:13: type
        {
        pushFollow(FOLLOW_type_in_synpred2345157);
        type();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred234

    // $ANTLR start synpred236
    public final void synpred236_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:949:17: ( '.' Identifier )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:949:17: '.' Identifier
        {
        match(input,29,FOLLOW_29_in_synpred2365198); if (failed) return ;
        match(input,Identifier,FOLLOW_Identifier_in_synpred2365200); if (failed) return ;

        }
    }
    // $ANTLR end synpred236

    // $ANTLR start synpred237
    public final void synpred237_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:949:35: ( identifierSuffix )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:949:35: identifierSuffix
        {
        pushFollow(FOLLOW_identifierSuffix_in_synpred2375205);
        identifierSuffix();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred237

    // $ANTLR start synpred242
    public final void synpred242_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:953:21: ( '.' Identifier )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:953:21: '.' Identifier
        {
        match(input,29,FOLLOW_29_in_synpred2425258); if (failed) return ;
        match(input,Identifier,FOLLOW_Identifier_in_synpred2425260); if (failed) return ;

        }
    }
    // $ANTLR end synpred242

    // $ANTLR start synpred243
    public final void synpred243_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:953:39: ( identifierSuffix )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:953:39: identifierSuffix
        {
        pushFollow(FOLLOW_identifierSuffix_in_synpred2435265);
        identifierSuffix();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred243

    // $ANTLR start synpred249
    public final void synpred249_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:960:10: ( '[' expression ']' )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:960:10: '[' expression ']'
        {
        match(input,48,FOLLOW_48_in_synpred2495341); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred2495343);
        expression();
        _fsp--;
        if (failed) return ;
        match(input,49,FOLLOW_49_in_synpred2495345); if (failed) return ;

        }
    }
    // $ANTLR end synpred249

    // $ANTLR start synpred262
    public final void synpred262_fragment() throws RecognitionException {   
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:986:29: ( '[' expression ']' )
        // /home/abyx/work/junit-converter/trunk/src/junitconverter/Java.g:986:29: '[' expression ']'
        {
        match(input,48,FOLLOW_48_in_synpred2625583); if (failed) return ;
        pushFollow(FOLLOW_expression_in_synpred2625585);
        expression();
        _fsp--;
        if (failed) return ;
        match(input,49,FOLLOW_49_in_synpred2625587); if (failed) return ;

        }
    }
    // $ANTLR end synpred262

    public final boolean synpred128() {
        backtracking++;
        int start = input.mark();
        try {
            synpred128_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred212() {
        backtracking++;
        int start = input.mark();
        try {
            synpred212_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred182() {
        backtracking++;
        int start = input.mark();
        try {
            synpred182_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred181() {
        backtracking++;
        int start = input.mark();
        try {
            synpred181_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred163() {
        backtracking++;
        int start = input.mark();
        try {
            synpred163_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred180() {
        backtracking++;
        int start = input.mark();
        try {
            synpred180_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred162() {
        backtracking++;
        int start = input.mark();
        try {
            synpred162_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred211() {
        backtracking++;
        int start = input.mark();
        try {
            synpred211_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred216() {
        backtracking++;
        int start = input.mark();
        try {
            synpred216_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred186() {
        backtracking++;
        int start = input.mark();
        try {
            synpred186_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred217() {
        backtracking++;
        int start = input.mark();
        try {
            synpred217_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred214() {
        backtracking++;
        int start = input.mark();
        try {
            synpred214_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred215() {
        backtracking++;
        int start = input.mark();
        try {
            synpred215_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred237() {
        backtracking++;
        int start = input.mark();
        try {
            synpred237_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred236() {
        backtracking++;
        int start = input.mark();
        try {
            synpred236_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred188() {
        backtracking++;
        int start = input.mark();
        try {
            synpred188_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred234() {
        backtracking++;
        int start = input.mark();
        try {
            synpred234_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred233() {
        backtracking++;
        int start = input.mark();
        try {
            synpred233_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred117() {
        backtracking++;
        int start = input.mark();
        try {
            synpred117_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred113() {
        backtracking++;
        int start = input.mark();
        try {
            synpred113_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred229() {
        backtracking++;
        int start = input.mark();
        try {
            synpred229_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred1() {
        backtracking++;
        int start = input.mark();
        try {
            synpred1_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred152() {
        backtracking++;
        int start = input.mark();
        try {
            synpred152_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred151() {
        backtracking++;
        int start = input.mark();
        try {
            synpred151_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred242() {
        backtracking++;
        int start = input.mark();
        try {
            synpred242_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred262() {
        backtracking++;
        int start = input.mark();
        try {
            synpred262_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred199() {
        backtracking++;
        int start = input.mark();
        try {
            synpred199_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred200() {
        backtracking++;
        int start = input.mark();
        try {
            synpred200_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred178() {
        backtracking++;
        int start = input.mark();
        try {
            synpred178_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred249() {
        backtracking++;
        int start = input.mark();
        try {
            synpred249_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred198() {
        backtracking++;
        int start = input.mark();
        try {
            synpred198_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred243() {
        backtracking++;
        int start = input.mark();
        try {
            synpred243_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred157() {
        backtracking++;
        int start = input.mark();
        try {
            synpred157_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }


 

    public static final BitSet FOLLOW_annotations_in_compilationUnit72 = new BitSet(new long[]{0x0000403F92000020L,0x0000000000000200L});
    public static final BitSet FOLLOW_packageDeclaration_in_compilationUnit86 = new BitSet(new long[]{0x0000403F9C000022L,0x0000000000000200L});
    public static final BitSet FOLLOW_importDeclaration_in_compilationUnit88 = new BitSet(new long[]{0x0000403F9C000022L,0x0000000000000200L});
    public static final BitSet FOLLOW_typeDeclaration_in_compilationUnit91 = new BitSet(new long[]{0x0000403F94000022L,0x0000000000000200L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_compilationUnit106 = new BitSet(new long[]{0x0000403F94000022L,0x0000000000000200L});
    public static final BitSet FOLLOW_typeDeclaration_in_compilationUnit108 = new BitSet(new long[]{0x0000403F94000022L,0x0000000000000200L});
    public static final BitSet FOLLOW_packageDeclaration_in_compilationUnit129 = new BitSet(new long[]{0x0000403F9C000022L,0x0000000000000200L});
    public static final BitSet FOLLOW_importDeclaration_in_compilationUnit133 = new BitSet(new long[]{0x0000403F9C000022L,0x0000000000000200L});
    public static final BitSet FOLLOW_typeDeclaration_in_compilationUnit136 = new BitSet(new long[]{0x0000403F94000022L,0x0000000000000200L});
    public static final BitSet FOLLOW_25_in_packageDeclaration156 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_qualifiedName_in_packageDeclaration158 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_packageDeclaration160 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_importDeclaration185 = new BitSet(new long[]{0x0000000010000010L});
    public static final BitSet FOLLOW_28_in_importDeclaration187 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_qualifiedName_in_importDeclaration190 = new BitSet(new long[]{0x0000000024000000L});
    public static final BitSet FOLLOW_29_in_importDeclaration193 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_30_in_importDeclaration195 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_importDeclaration199 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_typeDeclaration222 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_typeDeclaration232 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceModifiers_in_classOrInterfaceDeclaration255 = new BitSet(new long[]{0x0000402000000020L,0x0000000000000200L});
    public static final BitSet FOLLOW_classDeclaration_in_classOrInterfaceDeclaration258 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_classOrInterfaceDeclaration262 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceModifier_in_classOrInterfaceModifiers286 = new BitSet(new long[]{0x0000001F90000002L,0x0000000000000200L});
    public static final BitSet FOLLOW_annotation_in_classOrInterfaceModifier306 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_classOrInterfaceModifier319 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_classOrInterfaceModifier334 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_classOrInterfaceModifier346 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_classOrInterfaceModifier360 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_classOrInterfaceModifier373 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_classOrInterfaceModifier388 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_36_in_classOrInterfaceModifier404 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_modifiers426 = new BitSet(new long[]{0x00F0001F90000002L,0x0000000000000200L});
    public static final BitSet FOLLOW_normalClassDeclaration_in_classDeclaration449 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumDeclaration_in_classDeclaration463 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_37_in_normalClassDeclaration488 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_normalClassDeclaration490 = new BitSet(new long[]{0x000011C000000000L});
    public static final BitSet FOLLOW_typeParameters_in_normalClassDeclaration493 = new BitSet(new long[]{0x000010C000000000L});
    public static final BitSet FOLLOW_38_in_normalClassDeclaration508 = new BitSet(new long[]{0xFF00000000000010L});
    public static final BitSet FOLLOW_type_in_normalClassDeclaration510 = new BitSet(new long[]{0x0000108000000000L});
    public static final BitSet FOLLOW_39_in_normalClassDeclaration527 = new BitSet(new long[]{0xFF00000000000010L});
    public static final BitSet FOLLOW_typeList_in_normalClassDeclaration529 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_classBody_in_normalClassDeclaration541 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_40_in_typeParameters564 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_typeParameter_in_typeParameters566 = new BitSet(new long[]{0x0000060000000000L});
    public static final BitSet FOLLOW_41_in_typeParameters569 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_typeParameter_in_typeParameters571 = new BitSet(new long[]{0x0000060000000000L});
    public static final BitSet FOLLOW_42_in_typeParameters575 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_typeParameter594 = new BitSet(new long[]{0x0000004000000002L});
    public static final BitSet FOLLOW_38_in_typeParameter597 = new BitSet(new long[]{0xFF00000000000010L});
    public static final BitSet FOLLOW_typeBound_in_typeParameter599 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_typeBound628 = new BitSet(new long[]{0x0000080000000002L});
    public static final BitSet FOLLOW_43_in_typeBound631 = new BitSet(new long[]{0xFF00000000000010L});
    public static final BitSet FOLLOW_type_in_typeBound633 = new BitSet(new long[]{0x0000080000000002L});
    public static final BitSet FOLLOW_ENUM_in_enumDeclaration654 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_enumDeclaration656 = new BitSet(new long[]{0x0000108000000000L});
    public static final BitSet FOLLOW_39_in_enumDeclaration659 = new BitSet(new long[]{0xFF00000000000010L});
    public static final BitSet FOLLOW_typeList_in_enumDeclaration661 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_enumBody_in_enumDeclaration665 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_44_in_enumBody684 = new BitSet(new long[]{0x0000220004000010L,0x0000000000000200L});
    public static final BitSet FOLLOW_enumConstants_in_enumBody686 = new BitSet(new long[]{0x0000220004000000L});
    public static final BitSet FOLLOW_41_in_enumBody689 = new BitSet(new long[]{0x0000200004000000L});
    public static final BitSet FOLLOW_enumBodyDeclarations_in_enumBody692 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_45_in_enumBody695 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumConstant_in_enumConstants714 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_41_in_enumConstants717 = new BitSet(new long[]{0x0000000000000010L,0x0000000000000200L});
    public static final BitSet FOLLOW_enumConstant_in_enumConstants719 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_annotations_in_enumConstant744 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_enumConstant747 = new BitSet(new long[]{0x0000100000000002L,0x0000000000000004L});
    public static final BitSet FOLLOW_arguments_in_enumConstant750 = new BitSet(new long[]{0x0000100000000002L});
    public static final BitSet FOLLOW_classBody_in_enumConstant755 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_enumBodyDeclarations780 = new BitSet(new long[]{0xFFF0D13F94000032L,0x0000000000000200L});
    public static final BitSet FOLLOW_classBodyDeclaration_in_enumBodyDeclarations783 = new BitSet(new long[]{0xFFF0D13F94000032L,0x0000000000000200L});
    public static final BitSet FOLLOW_normalInterfaceDeclaration_in_interfaceDeclaration808 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationTypeDeclaration_in_interfaceDeclaration818 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_46_in_normalInterfaceDeclaration841 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_normalInterfaceDeclaration843 = new BitSet(new long[]{0x0000114000000000L});
    public static final BitSet FOLLOW_typeParameters_in_normalInterfaceDeclaration845 = new BitSet(new long[]{0x0000104000000000L});
    public static final BitSet FOLLOW_38_in_normalInterfaceDeclaration849 = new BitSet(new long[]{0xFF00000000000010L});
    public static final BitSet FOLLOW_typeList_in_normalInterfaceDeclaration851 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_interfaceBody_in_normalInterfaceDeclaration855 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_typeList878 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_41_in_typeList881 = new BitSet(new long[]{0xFF00000000000010L});
    public static final BitSet FOLLOW_type_in_typeList883 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_44_in_classBody908 = new BitSet(new long[]{0xFFF0F13F94000030L,0x0000000000000200L});
    public static final BitSet FOLLOW_classBodyDeclaration_in_classBody910 = new BitSet(new long[]{0xFFF0F13F94000030L,0x0000000000000200L});
    public static final BitSet FOLLOW_45_in_classBody913 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_44_in_interfaceBody936 = new BitSet(new long[]{0xFFF0E13F94000030L,0x0000000000000200L});
    public static final BitSet FOLLOW_interfaceBodyDeclaration_in_interfaceBody938 = new BitSet(new long[]{0xFFF0E13F94000030L,0x0000000000000200L});
    public static final BitSet FOLLOW_45_in_interfaceBody941 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_classBodyDeclaration960 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_classBodyDeclaration970 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_classBodyDeclaration973 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_classBodyDeclaration983 = new BitSet(new long[]{0xFF00C12000000030L,0x0000000000000200L});
    public static final BitSet FOLLOW_memberDecl_in_classBodyDeclaration985 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_genericMethodOrConstructorDecl_in_memberDecl1008 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_memberDeclaration_in_memberDecl1018 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_47_in_memberDecl1028 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_memberDecl1030 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_voidMethodDeclaratorRest_in_memberDecl1032 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_memberDecl1046 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_constructorDeclaratorRest_in_memberDecl1048 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_memberDecl1058 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_memberDecl1068 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_memberDeclaration1091 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_methodDeclaration_in_memberDeclaration1094 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fieldDeclaration_in_memberDeclaration1098 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeParameters_in_genericMethodOrConstructorDecl1118 = new BitSet(new long[]{0xFF00800000000010L});
    public static final BitSet FOLLOW_genericMethodOrConstructorRest_in_genericMethodOrConstructorDecl1120 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_genericMethodOrConstructorRest1144 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_47_in_genericMethodOrConstructorRest1148 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_genericMethodOrConstructorRest1151 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_methodDeclaratorRest_in_genericMethodOrConstructorRest1153 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_genericMethodOrConstructorRest1167 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_constructorDeclaratorRest_in_genericMethodOrConstructorRest1169 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_methodDeclaration1188 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_methodDeclaratorRest_in_methodDeclaration1190 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDeclarators_in_fieldDeclaration1213 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_fieldDeclaration1215 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_interfaceBodyDeclaration1242 = new BitSet(new long[]{0xFF00C12000000030L,0x0000000000000200L});
    public static final BitSet FOLLOW_interfaceMemberDecl_in_interfaceBodyDeclaration1244 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_interfaceBodyDeclaration1254 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceMethodOrFieldDecl_in_interfaceMemberDecl1273 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceGenericMethodDecl_in_interfaceMemberDecl1283 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_47_in_interfaceMemberDecl1293 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_interfaceMemberDecl1295 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_voidInterfaceMethodDeclaratorRest_in_interfaceMemberDecl1297 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_interfaceMemberDecl1307 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_interfaceMemberDecl1317 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_interfaceMethodOrFieldDecl1340 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_interfaceMethodOrFieldDecl1342 = new BitSet(new long[]{0x0009000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_interfaceMethodOrFieldRest_in_interfaceMethodOrFieldDecl1344 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constantDeclaratorsRest_in_interfaceMethodOrFieldRest1367 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_interfaceMethodOrFieldRest1369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceMethodDeclaratorRest_in_interfaceMethodOrFieldRest1379 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameters_in_methodDeclaratorRest1402 = new BitSet(new long[]{0x0005100004000000L});
    public static final BitSet FOLLOW_48_in_methodDeclaratorRest1405 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_methodDeclaratorRest1407 = new BitSet(new long[]{0x0005100004000000L});
    public static final BitSet FOLLOW_50_in_methodDeclaratorRest1420 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_qualifiedNameList_in_methodDeclaratorRest1422 = new BitSet(new long[]{0x0000100004000000L});
    public static final BitSet FOLLOW_methodBody_in_methodDeclaratorRest1438 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_methodDeclaratorRest1452 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameters_in_voidMethodDeclaratorRest1485 = new BitSet(new long[]{0x0004100004000000L});
    public static final BitSet FOLLOW_50_in_voidMethodDeclaratorRest1488 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_qualifiedNameList_in_voidMethodDeclaratorRest1490 = new BitSet(new long[]{0x0000100004000000L});
    public static final BitSet FOLLOW_methodBody_in_voidMethodDeclaratorRest1506 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_voidMethodDeclaratorRest1520 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameters_in_interfaceMethodDeclaratorRest1553 = new BitSet(new long[]{0x0005000004000000L});
    public static final BitSet FOLLOW_48_in_interfaceMethodDeclaratorRest1556 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_interfaceMethodDeclaratorRest1558 = new BitSet(new long[]{0x0005000004000000L});
    public static final BitSet FOLLOW_50_in_interfaceMethodDeclaratorRest1563 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_qualifiedNameList_in_interfaceMethodDeclaratorRest1565 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_interfaceMethodDeclaratorRest1569 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeParameters_in_interfaceGenericMethodDecl1592 = new BitSet(new long[]{0xFF00800000000010L});
    public static final BitSet FOLLOW_type_in_interfaceGenericMethodDecl1595 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_47_in_interfaceGenericMethodDecl1599 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_interfaceGenericMethodDecl1602 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_interfaceMethodDeclaratorRest_in_interfaceGenericMethodDecl1612 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameters_in_voidInterfaceMethodDeclaratorRest1635 = new BitSet(new long[]{0x0004000004000000L});
    public static final BitSet FOLLOW_50_in_voidInterfaceMethodDeclaratorRest1638 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_qualifiedNameList_in_voidInterfaceMethodDeclaratorRest1640 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_voidInterfaceMethodDeclaratorRest1644 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameters_in_constructorDeclaratorRest1667 = new BitSet(new long[]{0x0004100000000000L});
    public static final BitSet FOLLOW_50_in_constructorDeclaratorRest1670 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_qualifiedNameList_in_constructorDeclaratorRest1672 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_constructorBody_in_constructorDeclaratorRest1676 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_constantDeclarator1695 = new BitSet(new long[]{0x0009000000000000L});
    public static final BitSet FOLLOW_constantDeclaratorRest_in_constantDeclarator1697 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDeclarator_in_variableDeclarators1720 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_41_in_variableDeclarators1723 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_variableDeclarator_in_variableDeclarators1725 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_variableDeclaratorId_in_variableDeclarator1746 = new BitSet(new long[]{0x0008000000000002L});
    public static final BitSet FOLLOW_51_in_variableDeclarator1749 = new BitSet(new long[]{0xFF00900000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_variableInitializer_in_variableDeclarator1751 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constantDeclaratorRest_in_constantDeclaratorsRest1776 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_41_in_constantDeclaratorsRest1779 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_constantDeclarator_in_constantDeclaratorsRest1781 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_48_in_constantDeclaratorRest1803 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_constantDeclaratorRest1805 = new BitSet(new long[]{0x0009000000000000L});
    public static final BitSet FOLLOW_51_in_constantDeclaratorRest1809 = new BitSet(new long[]{0xFF00900000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_variableInitializer_in_constantDeclaratorRest1811 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_variableDeclaratorId1834 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_48_in_variableDeclaratorId1837 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_variableDeclaratorId1839 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_arrayInitializer_in_variableInitializer1860 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_variableInitializer1870 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_44_in_arrayInitializer1897 = new BitSet(new long[]{0xFF00B00000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_variableInitializer_in_arrayInitializer1900 = new BitSet(new long[]{0x0000220000000000L});
    public static final BitSet FOLLOW_41_in_arrayInitializer1903 = new BitSet(new long[]{0xFF00900000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_variableInitializer_in_arrayInitializer1905 = new BitSet(new long[]{0x0000220000000000L});
    public static final BitSet FOLLOW_41_in_arrayInitializer1910 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_45_in_arrayInitializer1917 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_modifier1936 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_modifier1946 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_modifier1956 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_modifier1966 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_modifier1976 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_modifier1986 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_modifier1996 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_modifier2006 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_53_in_modifier2016 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_54_in_modifier2026 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_55_in_modifier2036 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_36_in_modifier2046 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualifiedName_in_packageOrTypeName2065 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_enumConstantName2084 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualifiedName_in_typeName2103 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_type2117 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_48_in_type2120 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_type2122 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_primitiveType_in_type2129 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_48_in_type2132 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_type2134 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_Identifier_in_classOrInterfaceType2149 = new BitSet(new long[]{0x0000010020000002L});
    public static final BitSet FOLLOW_typeArguments_in_classOrInterfaceType2152 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_29_in_classOrInterfaceType2157 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_classOrInterfaceType2159 = new BitSet(new long[]{0x0000010020000002L});
    public static final BitSet FOLLOW_typeArguments_in_classOrInterfaceType2162 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_set_in_primitiveType0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_variableModifier2275 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_variableModifier2285 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_40_in_typeArguments2304 = new BitSet(new long[]{0xFF00000000000010L,0x0000000000000001L});
    public static final BitSet FOLLOW_typeArgument_in_typeArguments2306 = new BitSet(new long[]{0x0000060000000000L});
    public static final BitSet FOLLOW_41_in_typeArguments2309 = new BitSet(new long[]{0xFF00000000000010L,0x0000000000000001L});
    public static final BitSet FOLLOW_typeArgument_in_typeArguments2311 = new BitSet(new long[]{0x0000060000000000L});
    public static final BitSet FOLLOW_42_in_typeArguments2315 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_typeArgument2338 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_64_in_typeArgument2348 = new BitSet(new long[]{0x0000004000000002L,0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_typeArgument2351 = new BitSet(new long[]{0xFF00000000000010L});
    public static final BitSet FOLLOW_type_in_typeArgument2359 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualifiedName_in_qualifiedNameList2384 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_41_in_qualifiedNameList2387 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_qualifiedName_in_qualifiedNameList2389 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_66_in_formalParameters2410 = new BitSet(new long[]{0xFF00000800000010L,0x0000000000000208L});
    public static final BitSet FOLLOW_formalParameterDecls_in_formalParameters2412 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_67_in_formalParameters2415 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifiers_in_formalParameterDecls2438 = new BitSet(new long[]{0xFF00000000000010L});
    public static final BitSet FOLLOW_type_in_formalParameterDecls2440 = new BitSet(new long[]{0x0000000000000010L,0x0000000000000010L});
    public static final BitSet FOLLOW_formalParameterDeclsRest_in_formalParameterDecls2442 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDeclaratorId_in_formalParameterDeclsRest2465 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_41_in_formalParameterDeclsRest2468 = new BitSet(new long[]{0xFF00000800000010L,0x0000000000000200L});
    public static final BitSet FOLLOW_formalParameterDecls_in_formalParameterDeclsRest2470 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_68_in_formalParameterDeclsRest2482 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_variableDeclaratorId_in_formalParameterDeclsRest2484 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_block_in_methodBody2507 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_44_in_constructorBody2526 = new BitSet(new long[]{0xFF20F13F94001FF0L,0x0003E60000FBD3E6L});
    public static final BitSet FOLLOW_explicitConstructorInvocation_in_constructorBody2528 = new BitSet(new long[]{0xFF20F03F94001FF0L,0x0003E60000FBD3E6L});
    public static final BitSet FOLLOW_blockStatement_in_constructorBody2531 = new BitSet(new long[]{0xFF20F03F94001FF0L,0x0003E60000FBD3E6L});
    public static final BitSet FOLLOW_45_in_constructorBody2534 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation2554 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000022L});
    public static final BitSet FOLLOW_69_in_explicitConstructorInvocation2559 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_65_in_explicitConstructorInvocation2565 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_arguments_in_explicitConstructorInvocation2570 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_explicitConstructorInvocation2572 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primary_in_explicitConstructorInvocation2582 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_29_in_explicitConstructorInvocation2584 = new BitSet(new long[]{0x0000010000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation2586 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_explicitConstructorInvocation2591 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_arguments_in_explicitConstructorInvocation2595 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_explicitConstructorInvocation2597 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_qualifiedName2617 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_29_in_qualifiedName2620 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_qualifiedName2622 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_integerLiteral_in_literal2648 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FloatingPointLiteral_in_literal2658 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CharacterLiteral_in_literal2668 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_StringLiteral_in_literal2678 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_booleanLiteral_in_literal2688 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_70_in_literal2698 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_integerLiteral0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_booleanLiteral0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_annotations2787 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000200L});
    public static final BitSet FOLLOW_73_in_annotation2807 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_annotationName_in_annotation2809 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_annotation2813 = new BitSet(new long[]{0xFF00900000000FD0L,0x0003E600000003EEL});
    public static final BitSet FOLLOW_elementValuePairs_in_annotation2817 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_elementValue_in_annotation2821 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_67_in_annotation2826 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_annotationName2852 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_29_in_annotationName2855 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_annotationName2859 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_elementValuePair_in_elementValuePairs2883 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_41_in_elementValuePairs2886 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_elementValuePair_in_elementValuePairs2888 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_Identifier_in_elementValuePair2909 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_elementValuePair2911 = new BitSet(new long[]{0xFF00900000000FD0L,0x0003E600000003E6L});
    public static final BitSet FOLLOW_elementValue_in_elementValuePair2913 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalExpression_in_elementValue2936 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_elementValue2946 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_elementValueArrayInitializer_in_elementValue2956 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_44_in_elementValueArrayInitializer2979 = new BitSet(new long[]{0xFF00B20000000FD0L,0x0003E600000003E6L});
    public static final BitSet FOLLOW_elementValue_in_elementValueArrayInitializer2982 = new BitSet(new long[]{0x0000220000000000L});
    public static final BitSet FOLLOW_41_in_elementValueArrayInitializer2985 = new BitSet(new long[]{0xFF00900000000FD0L,0x0003E600000003E6L});
    public static final BitSet FOLLOW_elementValue_in_elementValueArrayInitializer2987 = new BitSet(new long[]{0x0000220000000000L});
    public static final BitSet FOLLOW_41_in_elementValueArrayInitializer2994 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_45_in_elementValueArrayInitializer2998 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_73_in_annotationTypeDeclaration3021 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_46_in_annotationTypeDeclaration3023 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_annotationTypeDeclaration3025 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_annotationTypeBody_in_annotationTypeDeclaration3027 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_44_in_annotationTypeBody3050 = new BitSet(new long[]{0xFFF0603F90000030L,0x0000000000000200L});
    public static final BitSet FOLLOW_annotationTypeElementDeclaration_in_annotationTypeBody3053 = new BitSet(new long[]{0xFFF0603F90000030L,0x0000000000000200L});
    public static final BitSet FOLLOW_45_in_annotationTypeBody3057 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_annotationTypeElementDeclaration3080 = new BitSet(new long[]{0xFF00402000000030L,0x0000000000000200L});
    public static final BitSet FOLLOW_annotationTypeElementRest_in_annotationTypeElementDeclaration3082 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_annotationTypeElementRest3105 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_annotationMethodOrConstantRest_in_annotationTypeElementRest3107 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_annotationTypeElementRest3109 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalClassDeclaration_in_annotationTypeElementRest3119 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_26_in_annotationTypeElementRest3121 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalInterfaceDeclaration_in_annotationTypeElementRest3132 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_26_in_annotationTypeElementRest3134 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumDeclaration_in_annotationTypeElementRest3145 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_26_in_annotationTypeElementRest3147 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationTypeDeclaration_in_annotationTypeElementRest3158 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_26_in_annotationTypeElementRest3160 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationMethodRest_in_annotationMethodOrConstantRest3184 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationConstantRest_in_annotationMethodOrConstantRest3194 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_annotationMethodRest3217 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_annotationMethodRest3219 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_67_in_annotationMethodRest3221 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000400L});
    public static final BitSet FOLLOW_defaultValue_in_annotationMethodRest3224 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDeclarators_in_annotationConstantRest3249 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_74_in_defaultValue3272 = new BitSet(new long[]{0xFF00900000000FD0L,0x0003E600000003E6L});
    public static final BitSet FOLLOW_elementValue_in_defaultValue3274 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_44_in_block3295 = new BitSet(new long[]{0xFF20F03F94001FF0L,0x0003E60000FBD3E6L});
    public static final BitSet FOLLOW_blockStatement_in_block3297 = new BitSet(new long[]{0xFF20F03F94001FF0L,0x0003E60000FBD3E6L});
    public static final BitSet FOLLOW_45_in_block3300 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclarationStatement_in_blockStatement3323 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_blockStatement3333 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_blockStatement3343 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclaration_in_localVariableDeclarationStatement3367 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_localVariableDeclarationStatement3369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifiers_in_localVariableDeclaration3388 = new BitSet(new long[]{0xFF00000000000010L});
    public static final BitSet FOLLOW_type_in_localVariableDeclaration3390 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_variableDeclarators_in_localVariableDeclaration3392 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifier_in_variableModifiers3415 = new BitSet(new long[]{0x0000000800000002L,0x0000000000000200L});
    public static final BitSet FOLLOW_block_in_statement3433 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASSERT_in_statement3443 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_expression_in_statement3445 = new BitSet(new long[]{0x0000000004000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_75_in_statement3448 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_expression_in_statement3450 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_statement3454 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_76_in_statement3464 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_parExpression_in_statement3466 = new BitSet(new long[]{0xFF20900004001FD0L,0x0003E60000FBD1E6L});
    public static final BitSet FOLLOW_statement_in_statement3468 = new BitSet(new long[]{0x0000000000000002L,0x0000000000002000L});
    public static final BitSet FOLLOW_77_in_statement3478 = new BitSet(new long[]{0xFF20900004001FD0L,0x0003E60000FBD1E6L});
    public static final BitSet FOLLOW_statement_in_statement3480 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_78_in_statement3492 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_statement3494 = new BitSet(new long[]{0xFF00800804000FD0L,0x0003E600000003E6L});
    public static final BitSet FOLLOW_forControl_in_statement3496 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_67_in_statement3498 = new BitSet(new long[]{0xFF20900004001FD0L,0x0003E60000FBD1E6L});
    public static final BitSet FOLLOW_statement_in_statement3500 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_79_in_statement3510 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_parExpression_in_statement3512 = new BitSet(new long[]{0xFF20900004001FD0L,0x0003E60000FBD1E6L});
    public static final BitSet FOLLOW_statement_in_statement3514 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_80_in_statement3524 = new BitSet(new long[]{0xFF20900004001FD0L,0x0003E60000FBD1E6L});
    public static final BitSet FOLLOW_statement_in_statement3526 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_79_in_statement3528 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_parExpression_in_statement3530 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_statement3532 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_81_in_statement3542 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_statement3544 = new BitSet(new long[]{0x0000000000000000L,0x0000000001040000L});
    public static final BitSet FOLLOW_catches_in_statement3556 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_82_in_statement3558 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_statement3560 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catches_in_statement3572 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_82_in_statement3586 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_statement3588 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_83_in_statement3608 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_parExpression_in_statement3610 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_44_in_statement3612 = new BitSet(new long[]{0x0000200000000000L,0x0000000002000400L});
    public static final BitSet FOLLOW_switchBlockStatementGroups_in_statement3614 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_45_in_statement3616 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_53_in_statement3626 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_parExpression_in_statement3628 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_statement3630 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_84_in_statement3640 = new BitSet(new long[]{0xFF00800004000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_expression_in_statement3642 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_statement3645 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_85_in_statement3655 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_expression_in_statement3657 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_statement3659 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_86_in_statement3669 = new BitSet(new long[]{0x0000000004000010L});
    public static final BitSet FOLLOW_Identifier_in_statement3671 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_statement3674 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_87_in_statement3684 = new BitSet(new long[]{0x0000000004000010L});
    public static final BitSet FOLLOW_Identifier_in_statement3686 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_statement3689 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_statement3699 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statementExpression_in_statement3710 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_statement3712 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_statement3722 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_75_in_statement3724 = new BitSet(new long[]{0xFF20900004001FD0L,0x0003E60000FBD1E6L});
    public static final BitSet FOLLOW_statement_in_statement3726 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catchClause_in_catches3749 = new BitSet(new long[]{0x0000000000000002L,0x0000000001000000L});
    public static final BitSet FOLLOW_catchClause_in_catches3752 = new BitSet(new long[]{0x0000000000000002L,0x0000000001000000L});
    public static final BitSet FOLLOW_88_in_catchClause3777 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_catchClause3779 = new BitSet(new long[]{0xFF00000800000010L,0x0000000000000200L});
    public static final BitSet FOLLOW_formalParameter_in_catchClause3781 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_67_in_catchClause3783 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_catchClause3785 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifiers_in_formalParameter3804 = new BitSet(new long[]{0xFF00000000000010L});
    public static final BitSet FOLLOW_type_in_formalParameter3806 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_variableDeclaratorId_in_formalParameter3808 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_switchBlockStatementGroup_in_switchBlockStatementGroups3836 = new BitSet(new long[]{0x0000000000000002L,0x0000000002000400L});
    public static final BitSet FOLLOW_switchLabel_in_switchBlockStatementGroup3863 = new BitSet(new long[]{0xFF20D03F94001FF2L,0x0003E60002FBD7E6L});
    public static final BitSet FOLLOW_blockStatement_in_switchBlockStatementGroup3866 = new BitSet(new long[]{0xFF20D03F94001FF2L,0x0003E60000FBD3E6L});
    public static final BitSet FOLLOW_89_in_switchLabel3890 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_constantExpression_in_switchLabel3892 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_75_in_switchLabel3894 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_89_in_switchLabel3904 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_enumConstantName_in_switchLabel3906 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_75_in_switchLabel3908 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_74_in_switchLabel3918 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_75_in_switchLabel3920 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enhancedForControl_in_forControl3951 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forInit_in_forControl3961 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_forControl3964 = new BitSet(new long[]{0xFF00800004000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_expression_in_forControl3966 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_forControl3969 = new BitSet(new long[]{0xFF00800000000FD2L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_forUpdate_in_forControl3971 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclaration_in_forInit3991 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expressionList_in_forInit4001 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifiers_in_enhancedForControl4024 = new BitSet(new long[]{0xFF00000000000010L});
    public static final BitSet FOLLOW_type_in_enhancedForControl4026 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_enhancedForControl4028 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_75_in_enhancedForControl4030 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_expression_in_enhancedForControl4032 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expressionList_in_forUpdate4051 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_66_in_parExpression4072 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_expression_in_parExpression4074 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_67_in_parExpression4076 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_expressionList4099 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_41_in_expressionList4102 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_expression_in_expressionList4104 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_expression_in_statementExpression4125 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_constantExpression4148 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalExpression_in_expression4171 = new BitSet(new long[]{0x0008050000000002L,0x00000003FC000000L});
    public static final BitSet FOLLOW_assignmentOperator_in_expression4174 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_expression_in_expression4176 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_51_in_assignmentOperator4201 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_90_in_assignmentOperator4211 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_91_in_assignmentOperator4221 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_92_in_assignmentOperator4231 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_93_in_assignmentOperator4241 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_94_in_assignmentOperator4251 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_95_in_assignmentOperator4261 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_96_in_assignmentOperator4271 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_97_in_assignmentOperator4281 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_40_in_assignmentOperator4302 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_40_in_assignmentOperator4306 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_assignmentOperator4310 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_42_in_assignmentOperator4344 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_assignmentOperator4348 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_assignmentOperator4352 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_assignmentOperator4356 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_42_in_assignmentOperator4387 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_assignmentOperator4391 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_assignmentOperator4395 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalOrExpression_in_conditionalExpression4424 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000001L});
    public static final BitSet FOLLOW_64_in_conditionalExpression4428 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_expression_in_conditionalExpression4430 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_75_in_conditionalExpression4432 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_expression_in_conditionalExpression4434 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalAndExpression_in_conditionalOrExpression4456 = new BitSet(new long[]{0x0000000000000002L,0x0000000400000000L});
    public static final BitSet FOLLOW_98_in_conditionalOrExpression4460 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_conditionalAndExpression_in_conditionalOrExpression4462 = new BitSet(new long[]{0x0000000000000002L,0x0000000400000000L});
    public static final BitSet FOLLOW_inclusiveOrExpression_in_conditionalAndExpression4484 = new BitSet(new long[]{0x0000000000000002L,0x0000000800000000L});
    public static final BitSet FOLLOW_99_in_conditionalAndExpression4488 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_inclusiveOrExpression_in_conditionalAndExpression4490 = new BitSet(new long[]{0x0000000000000002L,0x0000000800000000L});
    public static final BitSet FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression4512 = new BitSet(new long[]{0x0000000000000002L,0x0000001000000000L});
    public static final BitSet FOLLOW_100_in_inclusiveOrExpression4516 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression4518 = new BitSet(new long[]{0x0000000000000002L,0x0000001000000000L});
    public static final BitSet FOLLOW_andExpression_in_exclusiveOrExpression4540 = new BitSet(new long[]{0x0000000000000002L,0x0000002000000000L});
    public static final BitSet FOLLOW_101_in_exclusiveOrExpression4544 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_andExpression_in_exclusiveOrExpression4546 = new BitSet(new long[]{0x0000000000000002L,0x0000002000000000L});
    public static final BitSet FOLLOW_equalityExpression_in_andExpression4568 = new BitSet(new long[]{0x0000080000000002L});
    public static final BitSet FOLLOW_43_in_andExpression4572 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_equalityExpression_in_andExpression4574 = new BitSet(new long[]{0x0000080000000002L});
    public static final BitSet FOLLOW_instanceOfExpression_in_equalityExpression4596 = new BitSet(new long[]{0x0000000000000002L,0x000000C000000000L});
    public static final BitSet FOLLOW_set_in_equalityExpression4600 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_instanceOfExpression_in_equalityExpression4608 = new BitSet(new long[]{0x0000000000000002L,0x000000C000000000L});
    public static final BitSet FOLLOW_relationalExpression_in_instanceOfExpression4630 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
    public static final BitSet FOLLOW_104_in_instanceOfExpression4633 = new BitSet(new long[]{0xFF00000000000010L});
    public static final BitSet FOLLOW_type_in_instanceOfExpression4635 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_shiftExpression_in_relationalExpression4656 = new BitSet(new long[]{0x0000050000000002L});
    public static final BitSet FOLLOW_relationalOp_in_relationalExpression4660 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_shiftExpression_in_relationalExpression4662 = new BitSet(new long[]{0x0000050000000002L});
    public static final BitSet FOLLOW_40_in_relationalOp4697 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_relationalOp4701 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_42_in_relationalOp4731 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_relationalOp4735 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_40_in_relationalOp4756 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_42_in_relationalOp4767 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_shiftExpression4787 = new BitSet(new long[]{0x0000050000000002L});
    public static final BitSet FOLLOW_shiftOp_in_shiftExpression4791 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_additiveExpression_in_shiftExpression4793 = new BitSet(new long[]{0x0000050000000002L});
    public static final BitSet FOLLOW_40_in_shiftOp4824 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_40_in_shiftOp4828 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_42_in_shiftOp4860 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_shiftOp4864 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_shiftOp4868 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_42_in_shiftOp4898 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_shiftOp4902 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression4932 = new BitSet(new long[]{0x0000000000000002L,0x0000060000000000L});
    public static final BitSet FOLLOW_set_in_additiveExpression4936 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression4944 = new BitSet(new long[]{0x0000000000000002L,0x0000060000000000L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4966 = new BitSet(new long[]{0x0000000040000002L,0x0000180000000000L});
    public static final BitSet FOLLOW_set_in_multiplicativeExpression4970 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4984 = new BitSet(new long[]{0x0000000040000002L,0x0000180000000000L});
    public static final BitSet FOLLOW_105_in_unaryExpression5010 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression5012 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_106_in_unaryExpression5022 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression5024 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_109_in_unaryExpression5034 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression5036 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_110_in_unaryExpression5046 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression5048 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unaryExpressionNotPlusMinus_in_unaryExpression5058 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_111_in_unaryExpressionNotPlusMinus5077 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus5079 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_112_in_unaryExpressionNotPlusMinus5089 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus5091 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_castExpression_in_unaryExpressionNotPlusMinus5101 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primary_in_unaryExpressionNotPlusMinus5111 = new BitSet(new long[]{0x0001000020000002L,0x0000600000000000L});
    public static final BitSet FOLLOW_selector_in_unaryExpressionNotPlusMinus5113 = new BitSet(new long[]{0x0001000020000002L,0x0000600000000000L});
    public static final BitSet FOLLOW_set_in_unaryExpressionNotPlusMinus5116 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_66_in_castExpression5139 = new BitSet(new long[]{0xFF00000000000000L});
    public static final BitSet FOLLOW_primitiveType_in_castExpression5141 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_67_in_castExpression5143 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_unaryExpression_in_castExpression5145 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_66_in_castExpression5154 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_type_in_castExpression5157 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_expression_in_castExpression5161 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_67_in_castExpression5164 = new BitSet(new long[]{0xFF00800000000FD0L,0x00038000000001E6L});
    public static final BitSet FOLLOW_unaryExpressionNotPlusMinus_in_castExpression5166 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parExpression_in_primary5185 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_69_in_primary5195 = new BitSet(new long[]{0x0001000020000002L,0x0000000000000004L});
    public static final BitSet FOLLOW_29_in_primary5198 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_primary5200 = new BitSet(new long[]{0x0001000020000002L,0x0000000000000004L});
    public static final BitSet FOLLOW_identifierSuffix_in_primary5205 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_primary5219 = new BitSet(new long[]{0x0000000020000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_superSuffix_in_primary5221 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_primary5233 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_113_in_primary5243 = new BitSet(new long[]{0xFF00010000000010L});
    public static final BitSet FOLLOW_creator_in_primary5245 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_primary5255 = new BitSet(new long[]{0x0001000020000002L,0x0000000000000004L});
    public static final BitSet FOLLOW_29_in_primary5258 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_primary5260 = new BitSet(new long[]{0x0001000020000002L,0x0000000000000004L});
    public static final BitSet FOLLOW_identifierSuffix_in_primary5265 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primitiveType_in_primary5277 = new BitSet(new long[]{0x0001000020000000L});
    public static final BitSet FOLLOW_48_in_primary5280 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_primary5282 = new BitSet(new long[]{0x0001000020000000L});
    public static final BitSet FOLLOW_29_in_primary5286 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_37_in_primary5288 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_47_in_primary5298 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_29_in_primary5300 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_37_in_primary5302 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_identifierSuffix5322 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_identifierSuffix5324 = new BitSet(new long[]{0x0001000020000000L});
    public static final BitSet FOLLOW_29_in_identifierSuffix5328 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_37_in_identifierSuffix5330 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_identifierSuffix5341 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_expression_in_identifierSuffix5343 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_identifierSuffix5345 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_arguments_in_identifierSuffix5358 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_identifierSuffix5368 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_37_in_identifierSuffix5370 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_identifierSuffix5380 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_explicitGenericInvocation_in_identifierSuffix5382 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_identifierSuffix5392 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_69_in_identifierSuffix5394 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_identifierSuffix5404 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_identifierSuffix5406 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_arguments_in_identifierSuffix5408 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_identifierSuffix5418 = new BitSet(new long[]{0x0000000000000000L,0x0002000000000000L});
    public static final BitSet FOLLOW_113_in_identifierSuffix5420 = new BitSet(new long[]{0x0000010000000010L});
    public static final BitSet FOLLOW_innerCreator_in_identifierSuffix5422 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_creator5441 = new BitSet(new long[]{0xFF00000000000010L});
    public static final BitSet FOLLOW_createdName_in_creator5443 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_classCreatorRest_in_creator5445 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_createdName_in_creator5455 = new BitSet(new long[]{0x0001000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_arrayCreatorRest_in_creator5458 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classCreatorRest_in_creator5462 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_createdName5482 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primitiveType_in_createdName5492 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_innerCreator5516 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_innerCreator5520 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_classCreatorRest_in_innerCreator5522 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_arrayCreatorRest5541 = new BitSet(new long[]{0xFF02800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_49_in_arrayCreatorRest5555 = new BitSet(new long[]{0x0001100000000000L});
    public static final BitSet FOLLOW_48_in_arrayCreatorRest5558 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_arrayCreatorRest5560 = new BitSet(new long[]{0x0001100000000000L});
    public static final BitSet FOLLOW_arrayInitializer_in_arrayCreatorRest5564 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_arrayCreatorRest5578 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_arrayCreatorRest5580 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_48_in_arrayCreatorRest5583 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_expression_in_arrayCreatorRest5585 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_arrayCreatorRest5587 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_48_in_arrayCreatorRest5592 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_arrayCreatorRest5594 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_arguments_in_classCreatorRest5627 = new BitSet(new long[]{0x0000100000000002L});
    public static final BitSet FOLLOW_classBody_in_classCreatorRest5629 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_explicitGenericInvocation5655 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_explicitGenericInvocation5657 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_arguments_in_explicitGenericInvocation5659 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_40_in_nonWildcardTypeArguments5682 = new BitSet(new long[]{0xFF00000000000010L});
    public static final BitSet FOLLOW_typeList_in_nonWildcardTypeArguments5684 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_nonWildcardTypeArguments5686 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_selector5709 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_selector5711 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
    public static final BitSet FOLLOW_arguments_in_selector5714 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_selector5726 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_69_in_selector5728 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_selector5738 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_selector5740 = new BitSet(new long[]{0x0000000020000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_superSuffix_in_selector5742 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_selector5752 = new BitSet(new long[]{0x0000000000000000L,0x0002000000000000L});
    public static final BitSet FOLLOW_113_in_selector5754 = new BitSet(new long[]{0x0000010000000010L});
    public static final BitSet FOLLOW_innerCreator_in_selector5756 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_selector5766 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_expression_in_selector5768 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_selector5770 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arguments_in_superSuffix5793 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_superSuffix5803 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_superSuffix5805 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
    public static final BitSet FOLLOW_arguments_in_superSuffix5808 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_66_in_arguments5829 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001EEL});
    public static final BitSet FOLLOW_expressionList_in_arguments5831 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_67_in_arguments5834 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_73_in_synpred168 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_explicitConstructorInvocation_in_synpred1132528 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_synpred1172554 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000022L});
    public static final BitSet FOLLOW_set_in_synpred1172558 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_arguments_in_synpred1172570 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_synpred1172572 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_synpred1282787 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclarationStatement_in_synpred1513323 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_synpred1523333 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_77_in_synpred1573478 = new BitSet(new long[]{0xFF20900004001FD0L,0x0003E60000FBD1E6L});
    public static final BitSet FOLLOW_statement_in_synpred1573480 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catches_in_synpred1623556 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_82_in_synpred1623558 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_synpred1623560 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catches_in_synpred1633572 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_switchLabel_in_synpred1783863 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_89_in_synpred1803890 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_constantExpression_in_synpred1803892 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_75_in_synpred1803894 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_89_in_synpred1813904 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_enumConstantName_in_synpred1813906 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_75_in_synpred1813908 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enhancedForControl_in_synpred1823951 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclaration_in_synpred1863991 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentOperator_in_synpred1884174 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_expression_in_synpred1884176 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_40_in_synpred1984292 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_40_in_synpred1984294 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_synpred1984296 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_42_in_synpred1994332 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_synpred1994334 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_synpred1994336 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_synpred1994338 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_42_in_synpred2004377 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_synpred2004379 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_synpred2004381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_40_in_synpred2114689 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_synpred2114691 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_42_in_synpred2124723 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_synpred2124725 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_shiftOp_in_synpred2144791 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_additiveExpression_in_synpred2144793 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_40_in_synpred2154816 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_40_in_synpred2154818 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_42_in_synpred2164850 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_synpred2164852 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_synpred2164854 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_42_in_synpred2174890 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_synpred2174892 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_castExpression_in_synpred2295101 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_66_in_synpred2335139 = new BitSet(new long[]{0xFF00000000000000L});
    public static final BitSet FOLLOW_primitiveType_in_synpred2335141 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_67_in_synpred2335143 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_unaryExpression_in_synpred2335145 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_synpred2345157 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_synpred2365198 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_synpred2365200 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifierSuffix_in_synpred2375205 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_synpred2425258 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_synpred2425260 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifierSuffix_in_synpred2435265 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_synpred2495341 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_expression_in_synpred2495343 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_synpred2495345 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_synpred2625583 = new BitSet(new long[]{0xFF00800000000FD0L,0x0003E600000001E6L});
    public static final BitSet FOLLOW_expression_in_synpred2625585 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_synpred2625587 = new BitSet(new long[]{0x0000000000000002L});

}