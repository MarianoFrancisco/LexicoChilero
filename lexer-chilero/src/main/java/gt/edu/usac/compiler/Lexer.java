package gt.edu.usac.compiler;/*
Copyright (c) 2000 The Regents of the University of California.
All rights reserved.

Permission to use, copy, modify, and distribute this software for any
purpose, without fee, and without written agreement is hereby granted,
provided that the above copyright notice and the following two
paragraphs appear in all copies of this software.

IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF
CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
*/

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java_cup.runtime.Symbol;
import java.util.Scanner;
//mis import
import gt.edu.usac.compiler.CoolLexer;
import picocli.CommandLine;
import java.util.concurrent.Callable;
import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.nio.file.Files;
@CommandLine.Command(name="Lexer Cool-Chilero",mixinStandardHelpOptions = true,version="1.0.0",
		description = "Lexer for cool programming language")
/** The lexer driver class */
class Lexer implements Callable<Integer>
{

	/** Loops over lexed tokens, printing them out to the console */
	@CommandLine.Option(names = {"-f","--file"}, description = "Archivo para leer",required = false)
	private File file;
	@Override
	public Integer call() throws Exception {
		if (file!=null){
			BufferedReader bfr = Files.newBufferedReader(file.toPath());
			CoolLexer coolLexer = new CoolLexer(bfr);
			Symbol token;
			while ((token=coolLexer.next_token()).sym != TokenConstants.EOF) {
				Utilities.dumpToken(System.out, coolLexer.get_curr_lineno(), token);
			}
		}else{
			Scanner scanner = new Scanner(System.in);
			String input = "";
			while (!input.equals("salir")) {
				System.out.print("Ingresa tu cadena >");
				input = scanner.nextLine();
				if (input.equals("salir")){
					break;
				}
				CoolLexer coolLexer = new CoolLexer(new StringReader(input));
				Symbol token = coolLexer.next_token();
				Utilities.dumpToken(System.out, coolLexer.get_curr_lineno(), token);
			}
		}
		return 0;
	}
	public static void main(String[] args) {
		if (args.length<1||args[0].equals("--help")||args[0].equals("-h")||args[0].equals("--file")||args[0].equals("-f")||args[0].equals("-V")||args[0].equals("--version")){
			int exitCode = new CommandLine(new Lexer()).execute(args);
			System.exit(exitCode);

			System.out.println(args[0]);
		}else{args = Flags.handleFlags(args);
			for (int i = 0; i < args.length; i++) {
				FileReader file = null;
				try {
					file = new FileReader(args[i]);
					System.out.println("#name \"" + args[i] + "\"");
					CoolLexer lexer = new CoolLexer(file);
					lexer.set_filename(args[i]);
					Symbol s;
					while ((s = lexer.next_token()).sym != TokenConstants.EOF) {
						Utilities.dumpToken(System.out, lexer.get_curr_lineno(), s);
					}
				} catch (FileNotFoundException ex) {
					Utilities.fatalError("Could not open input file " + args[i]);
				} catch (IOException ex) {
					Utilities.fatalError("Unexpected exception in lexer");
				}
			}
		}}
}