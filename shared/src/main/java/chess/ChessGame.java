package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    TeamColor currentPlayerTurn;
    ChessBoard board;

    public ChessGame() {
        this.currentPlayerTurn = TeamColor.WHITE;
        this.board = new ChessBoard();
        this.board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.currentPlayerTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.currentPlayerTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */

    private ChessBoard simulateMove(ChessMove move, ChessBoard board) {
        ChessPiece pieceToMove = board.getPiece(move.getStartPosition());
        board.addPiece(move.getEndPosition(), pieceToMove);
        board.removePiece(move.getStartPosition());
        return board;
    }

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        // get the piece at that position
        ChessPiece piece = this.board.getPiece(startPosition);

        // get its pieceMoves collection
        Collection<ChessMove> possibleMoves = piece.pieceMoves(this.board, startPosition);

        // see if after making any of the moves the teams color is in check
        // need a function that simulates a move and gives me back a board state
        // then I can run isInCheck on the side that made the move

            // if it is, remove it

            // else, add it

        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // Part 1: Find the King

        // Making space to save kingPos outside of if statement
        ChessPosition kingPos = null;

        // Search the board space for the king
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {

                // Grabbing piece at the square
                ChessPiece piece = this.board.getPiece(new ChessPosition(row, col));

                // Checking that it's not empty before we call getPieceType
                if (piece != null) {

                    // Checking if it's the king we want
                    if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {

                        // Saving its position outside the loop
                        kingPos = new ChessPosition(row, col);
                    }
                }

            }
        }

        // Part 2: Figure out if he's under attack

        // Checking all positions on the board
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {

                // Getting the piece at the position
                ChessPiece piece = this.board.getPiece(new ChessPosition(row, col));

                // Checking that it's not empty
                if (piece != null) {

                    // Checking that it's and enemy piece
                    if (piece.getTeamColor() != teamColor) {

                        // Getting its moveset
                        Collection<ChessMove> possibleMoves = piece.pieceMoves(this.board, new ChessPosition(row, col));

                        // Checking all moves
                        for (ChessMove move : possibleMoves) {

                            // Checking if the end position is same as kings position
                            if (move.getEndPosition().equals(kingPos)) {
                                return true;
                            }
                        }
                    }
                }

            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return currentPlayerTurn == chessGame.currentPlayerTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentPlayerTurn, board);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "currentPlayerTurn=" + currentPlayerTurn +
                ", board=" + board +
                '}';
    }
}
