package passoff.chess;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChessGameTest {

    @Test
    void testKingNotInCheck() {
        ChessGame game = new ChessGame();
        ChessBoard board = new ChessBoard();

        board.addPiece(
                new ChessPosition(1, 5),
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING)
        );

        board.addPiece(
                new ChessPosition(8, 5),
                new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING)
        );

        game.setBoard(board);

        assertFalse(game.isInCheck(ChessGame.TeamColor.WHITE));
    }

    @Test
    void testKingInCheckByRook() {
        ChessGame game = new ChessGame();
        ChessBoard board = new ChessBoard();

        board.addPiece(
                new ChessPosition(1, 5),
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING)
        );

        board.addPiece(
                new ChessPosition(1, 8),
                new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK)
        );

        game.setBoard(board);

        assertTrue(game.isInCheck(ChessGame.TeamColor.WHITE));
    }

    @Test
    void testRookBlockedByPiece() {
        ChessGame game = new ChessGame();
        ChessBoard board = new ChessBoard();

        board.addPiece(
                new ChessPosition(1, 5),
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING)
        );

        board.addPiece(
                new ChessPosition(1, 6),
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN)
        );

        board.addPiece(
                new ChessPosition(1, 8),
                new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK)
        );

        game.setBoard(board);

        assertFalse(game.isInCheck(ChessGame.TeamColor.WHITE));
    }

    @Test
    void testKingInCheckByBishop() {
        ChessGame game = new ChessGame();
        ChessBoard board = new ChessBoard();

        board.addPiece(
                new ChessPosition(4, 4),
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING)
        );

        board.addPiece(
                new ChessPosition(7, 7),
                new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP)
        );

        game.setBoard(board);

        assertTrue(game.isInCheck(ChessGame.TeamColor.WHITE));
    }

    @Test
    void testKingInCheckByKnight() {
        ChessGame game = new ChessGame();
        ChessBoard board = new ChessBoard();

        board.addPiece(
                new ChessPosition(4, 4),
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING)
        );

        board.addPiece(
                new ChessPosition(6, 5),
                new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT)
        );

        game.setBoard(board);

        assertTrue(game.isInCheck(ChessGame.TeamColor.WHITE));
    }

    @Test
    void testKingInCheckByQueen() {
        ChessGame game = new ChessGame();
        ChessBoard board = new ChessBoard();

        board.addPiece(
                new ChessPosition(1, 5),
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING)
        );

        board.addPiece(
                new ChessPosition(5, 5),
                new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN)
        );

        game.setBoard(board);

        assertTrue(game.isInCheck(ChessGame.TeamColor.WHITE));
    }

    @Test
    void testKingInCheckByPawn() {
        ChessGame game = new ChessGame();
        ChessBoard board = new ChessBoard();

        board.addPiece(
                new ChessPosition(4, 4),
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING)
        );

        board.addPiece(
                new ChessPosition(5, 3),
                new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN)
        );

        game.setBoard(board);

        assertTrue(game.isInCheck(ChessGame.TeamColor.WHITE));
    }

    @Test
    void testBlackKingInCheckByRook() {
        ChessGame game = new ChessGame();
        ChessBoard board = new ChessBoard();

        board.addPiece(
                new ChessPosition(8, 5),
                new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING)
        );

        board.addPiece(
                new ChessPosition(1, 5),
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK)
        );

        game.setBoard(board);

        assertTrue(game.isInCheck(ChessGame.TeamColor.BLACK));
    }
}
