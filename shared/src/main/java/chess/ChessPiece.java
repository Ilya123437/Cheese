package chess;

import java.util.Collection;
import java.util.ArrayList;
/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessPiece.PieceType piece;
    private final ChessGame.TeamColor color;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.piece = type;
        this.color = pieceColor;
    }

    /**
     * The various differenhessGame.TeamCot chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.piece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return (piece.equals(that.piece) && color.equals(that.color));
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.color, this.piece);
    }

    @Override
    public int hashCode() {
        int result = (this.piece != null ? this.piece.hashCode() : 0);
        result = 31 * result + (this.color != null ? this.color.hashCode() : 0);
        return result;
    }

    private void addPromotionMoves(ChessPosition pos1, ChessPosition pos2, Collection<ChessMove> moveSet){
        moveSet.add(new ChessMove(pos1, pos2, PieceType.QUEEN));
        moveSet.add(new ChessMove(pos1, pos2, PieceType.KNIGHT));
        moveSet.add(new ChessMove(pos1, pos2, PieceType.BISHOP));
        moveSet.add(new ChessMove(pos1, pos2, PieceType.ROOK));
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        int[][] moves = new int[][]{{0, 0}};
        boolean repeatability = false;
        if (this.piece == PieceType.ROOK) {
            moves = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            repeatability = true;
        } else if (this.piece == PieceType.BISHOP) {
            moves = new int[][]{{1, -1}, {-1, 1}, {-1, -1}, {1, 1}};
            repeatability = true;
        } else if (this.piece == PieceType.QUEEN) {
            moves = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, -1}, {-1, 1}, {-1, -1}, {1, 1}};
            repeatability = true;
        } else if (this.piece == PieceType.KING) {
            moves = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, -1}, {-1, 1}, {-1, -1}, {1, 1}};
        } else if (this.piece == PieceType.KNIGHT) {
            moves = new int[][]{{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};

        // Detecting pawns, this is the fun part lol
        } else if (this.piece == PieceType.PAWN) {
            return getPawnMoves(board, myPosition);
        }

        int currentRowMut = myPosition.getRow();
        int currentColMut = myPosition.getColumn();


        for (int[] move : moves) {
            if (repeatability) {
                boolean needToStop = false;

                // I reset these if case 2 was hit
                currentRowMut = myPosition.getRow();
                currentColMut = myPosition.getColumn();

                while (!needToStop) {
                    // We make our move
                    ChessPosition nextPosition = new ChessPosition(currentRowMut + move[0], currentColMut + move[1]);

                    // Case 1: it's off the board
                    if (nextPosition.getRow() > 8 || nextPosition.getRow() < 1 || nextPosition.getColumn() > 8 || nextPosition.getColumn() < 1) {
                        needToStop = true;

                    // Case 2: it's on the board and it's an empty square
                    } else if (board.getPiece(nextPosition) == null) {
                        currentRowMut = currentRowMut + move[0];
                        currentColMut = currentColMut + move[1];
                        ChessPosition newPos = new ChessPosition(currentRowMut, currentColMut);
                        ChessMove newMove = new ChessMove(myPosition, newPos, null);
                        possibleMoves.add(newMove);

                    // Case 3: we run into a piece and it's not the same color
                    } else if (board.getPiece(nextPosition).getTeamColor() != this.color) {
                        currentRowMut = currentRowMut + move[0];
                        currentColMut = currentColMut + move[1];
                        ChessPosition newPos = new ChessPosition(currentRowMut, currentColMut);
                        ChessMove newMove = new ChessMove(myPosition, newPos, null);
                        possibleMoves.add(newMove);
                        needToStop = true;

                    // Case 4: we run into our own piece
                    } else {
                        needToStop = true;

                    }
                }
            } else {
                ChessPosition nextPosition = new ChessPosition(currentRowMut + move[0], currentColMut + move[1]);
                ChessMove nextMove = new ChessMove(myPosition, nextPosition, null);
                // Case 1: it's off the board
                if (nextPosition.getRow() > 8 || nextPosition.getRow() < 1 || nextPosition.getColumn() > 8 || nextPosition.getColumn() < 1) {
                    continue;

                // Case 2: it's on the board and it's an empty square
                } else if (board.getPiece(nextPosition) == null) {
                    possibleMoves.add(nextMove);

                // Case 3: we run into a piece and it's not the same color
                } else if (board.getPiece(nextPosition).getTeamColor() != this.color) {
                    possibleMoves.add(nextMove);
                }

            }
        }
        return possibleMoves;
    }

    private Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        if (this.color == ChessGame.TeamColor.WHITE) {
            getWhitePawnMoves(board, myPosition, possibleMoves);
        } else {
            getBlackPawnMoves(board, myPosition, possibleMoves);
        }
        return possibleMoves;
    }

    private void getWhitePawnMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        // Check the diagonals
        ChessPosition diagonalLeft = new ChessPosition(row + 1, col - 1);
        ChessPosition diagonalRight = new ChessPosition(row + 1, col + 1);

        // Check if there is a black piece on the left diagonal
        if (col > 1 && board.getPiece(diagonalLeft) != null) {
            if (board.getPiece(diagonalLeft).getTeamColor() == ChessGame.TeamColor.BLACK) {
                addPawnMove(myPosition, diagonalLeft, possibleMoves);
            }
        }
        // Check if there is a black piece on the right diagonal
        if (col < 8 && board.getPiece(diagonalRight) != null) {
            if (board.getPiece(diagonalRight).getTeamColor() == ChessGame.TeamColor.BLACK) {
                addPawnMove(myPosition, diagonalRight, possibleMoves);
            }
        }
        // Check out of bounds
        if (row < 8) {
            ChessPosition straightAhead = new ChessPosition(row + 1, col);
            ChessPosition straightAhead2 = new ChessPosition(row + 2, col);
            // Check straight ahead
            if (board.getPiece(straightAhead) == null) {
                addPawnMove(myPosition, straightAhead, possibleMoves);
                // Checking starting position
                if (row == 2) {
                    // Check straight ahead 2 spaces
                    if (board.getPiece(straightAhead2) == null) {
                        addPawnMove(myPosition, straightAhead2, possibleMoves);
                    }
                }
            }
        }
    }

    private void getBlackPawnMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        // Check the diagonals
        ChessPosition diagonalLeft = new ChessPosition(row - 1, col + 1);
        ChessPosition diagonalRight = new ChessPosition(row - 1, col - 1);

        // Check if there is a white piece on the left diagonal
        if (col < 8 && board.getPiece(diagonalLeft) != null) {
            if (board.getPiece(diagonalLeft).getTeamColor() == ChessGame.TeamColor.WHITE) {
                addPawnMove(myPosition, diagonalLeft, possibleMoves);
            }
        }
        // Check if there is a white piece on the right diagonal
        if (col > 1 && board.getPiece(diagonalRight) != null) {
            if (board.getPiece(diagonalRight).getTeamColor() == ChessGame.TeamColor.WHITE) {
                addPawnMove(myPosition, diagonalRight, possibleMoves);
            }
        }
        // Check out of bounds
        if (row > 1) {
            ChessPosition straightAhead = new ChessPosition(row - 1, col);
            ChessPosition straightAhead2 = new ChessPosition(row - 2, col);

            // Check straight ahead
            if (board.getPiece(straightAhead) == null) {
                addPawnMove(myPosition, straightAhead, possibleMoves);

                // Checking starting position
                if (row == 7) {
                    // Check straight ahead 2 spaces
                    if (board.getPiece(straightAhead2) == null) {
                        addPawnMove(myPosition, straightAhead2, possibleMoves);
                    }
                }
            }
        }
    }

    private void addPawnMove(ChessPosition start, ChessPosition end, Collection<ChessMove> possibleMoves) {
        if (end.getRow() == 8 || end.getRow() == 1) {
            addPromotionMoves(start, end, possibleMoves);
        } else {
            possibleMoves.add(new ChessMove(start, end, null));
        }
    }
}
