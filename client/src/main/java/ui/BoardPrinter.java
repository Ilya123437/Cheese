package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class BoardPrinter {
    private static final String ORIGINAL = "\u001b[0m";
    private static final String LIGHT_SQUARE = "\u001b[48;5;180m";
    private static final String DARK_SQUARE = "\u001b[48;5;94m";
    private static final String BORDER_BACKGROUND = "\u001b[48;5;240m";
    private static final String BORDER_TEXT = "\u001b[38;5;255m";
    private static final String WHITE_PIECE_COLOR = "\u001b[38;5;196m";
    private static final String BLACK_PIECE_COLOR = "\u001b[38;5;21m";
    private static final String EMPTY = "\u2003";

    public static void drawBoard(ChessBoard board, ChessGame.TeamColor perspective) {
        boolean whitePerspective = true;
        if (perspective == ChessGame.TeamColor.BLACK) {
            whitePerspective = false;
        }
        printLetters(whitePerspective);
        for (int i = 0; i < 8; i++) {
            int row;
            if (whitePerspective) {
                row = 8 - i;
            } else {
                row = 1 + i;
            }
            printRowNumber(row);
            for (int j = 0; j < 8; j++) {
                int col;
                if (whitePerspective) {
                    col = 1 + j;
                } else {
                    col = 8 - j;
                }
                printSquare(board, row, col);
            }
            printRowNumber(row);
            System.out.println();
        }
        printLetters(whitePerspective);
    }

    private static void printSquare(ChessBoard board, int row, int col) {
        int total = row + col;

        if (total % 2 == 1) {
            System.out.print(LIGHT_SQUARE);
        } else {
            System.out.print(DARK_SQUARE);
        }

        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        if (piece == null) {
            System.out.print(" " + EMPTY + " ");
        } else {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                System.out.print(WHITE_PIECE_COLOR);
            } else {
                System.out.print(BLACK_PIECE_COLOR);
            }
            System.out.print(" " + getSymbol(piece.getPieceType()) + " ");
        }
        System.out.print(ORIGINAL);
    }

    private static String getSymbol(ChessPiece.PieceType type) {
        if (type == ChessPiece.PieceType.KING) {
            return "♚";
        } else if (type == ChessPiece.PieceType.QUEEN) {
            return "♛";
        } else if (type == ChessPiece.PieceType.BISHOP) {
            return "♝";
        } else if (type == ChessPiece.PieceType.KNIGHT) {
            return "♞";
        } else if (type == ChessPiece.PieceType.ROOK) {
            return "♜";
        } else {
            return "♟";
        }
    }

    private static void printRowNumber(int row) {
        System.out.print(BORDER_BACKGROUND + BORDER_TEXT + " " + row + " " + ORIGINAL);
    }

    private static void printLetters(boolean whitePerspective) {
        String[] whiteOrder = {"a", "b", "c", "d", "e", "f", "g", "h"};
        String[] blackOrder = {"h", "g", "f", "e", "d", "c", "b", "a"};
        String[] letters;
        if (whitePerspective) {
            letters = whiteOrder;
        } else {
            letters = blackOrder;
        }
        System.out.print(BORDER_BACKGROUND + BORDER_TEXT + "   ");
        for (String letter : letters) {
            System.out.print(" " + letter + EMPTY);
        }
        System.out.println("   " + ORIGINAL);
    }
}