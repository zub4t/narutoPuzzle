package mpjp.client;

import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.user.client.rpc.AsyncCallback;

import mpjp.shared.PieceStatus;
import mpjp.shared.PuzzleLayout;
import mpjp.shared.PuzzleView;
import mpjp.shared.geom.CurveTo;
import mpjp.shared.geom.LineTo;
import mpjp.shared.geom.PieceShape;
import mpjp.shared.geom.Point;
import mpjp.shared.geom.QuadTo;
import mpjp.shared.geom.Segment;

public class Paint {
	BundleMap bundleMap = null;
	private final Logger logger = java.util.logging.Logger.getLogger("ASW-Trab 3");

	public void setBundleMap(BundleMap bundleMap) {
		this.bundleMap = bundleMap;
	}

	private final MPJPServiceAsync mpjpService = GWT.create(MPJPService.class);

	public void drawBasedOnServer(String workspaceId) {

		mpjpService.getCurrentLayout(workspaceId, new AsyncCallback<PuzzleLayout>() {
			@Override
			public void onSuccess(PuzzleLayout puzzleLayout) {
				bundleMap.mapPuzzleLayout.put(workspaceId, puzzleLayout);
				mpjpService.getPuzzleView(workspaceId, new AsyncCallback<PuzzleView>() {

					@Override
					public void onSuccess(PuzzleView puzzleView) {
						bundleMap.mapPuzzleView.put(workspaceId, puzzleView);
						bundleMap.mapCanvas.get(workspaceId).getContext2d().save();
						draw(workspaceId, bundleMap.mapImageElement.get(workspaceId));
						bundleMap.mapCanvas.get(workspaceId).getContext2d().restore();
					}

					@Override
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, caught.toString());
					}
				});
			}

			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.toString());

			}

		});

	}

	public void draw(String workspaceId, ImageElement image) {
		Canvas canvas = bundleMap.mapCanvas.get(workspaceId);
		Context2d context2d = canvas.getContext2d();
		PuzzleLayout puzzleLayout = bundleMap.mapPuzzleLayout.get(workspaceId);
		PuzzleView puzzleView = bundleMap.mapPuzzleView.get(workspaceId);
		Map<Integer, PieceStatus> pieces = puzzleLayout.getPieces();
		canvas.setCoordinateSpaceWidth(canvas.getCoordinateSpaceWidth());

		for (Entry<Integer, PieceStatus> entry : pieces.entrySet()) {

			context2d.save();
			PieceStatus pieceStatus = entry.getValue();
			Point center = pieceStatus.getPosition();

			context2d.translate(center.getX(), center.getY());

			int id = entry.getValue().getId();

			PieceShape pieceShape = puzzleView.getPieceShape(id);
			Point start = pieceShape.getStartPoint();

			context2d.beginPath();
			context2d.moveTo(start.getX(), start.getY());

			for (Segment segment : pieceShape.getSegments()) {

				if (segment instanceof LineTo) {

					LineTo lineTo = (LineTo) segment;
					Point endPoint = lineTo.getEndPoint();

					context2d.lineTo(endPoint.getX(), endPoint.getY());

				} else if (segment instanceof QuadTo) {

					QuadTo quadTo = (QuadTo) segment;
					Point endPoint = quadTo.getEndPoint();
					Point controlPoint = quadTo.getControlPoint();

					context2d.quadraticCurveTo(controlPoint.getX(), controlPoint.getY(), endPoint.getX(),
							endPoint.getY());

				} else if (segment instanceof CurveTo) {

					CurveTo curveTo = (CurveTo) segment;
					Point endPoint = curveTo.getEndPoint();
					Point controlPoint1 = curveTo.getControlPoint1();
					Point controlPoint2 = curveTo.getControlPoint2();

					context2d.bezierCurveTo(controlPoint1.getX(), controlPoint1.getY(), controlPoint2.getX(),
							controlPoint2.getY(), endPoint.getX(), endPoint.getY());

				}

			}

			context2d.setStrokeStyle("black");

			context2d.stroke();
			Point location = puzzleView.getStandardPieceLocation(id);
			int x = (int) -location.getX();
			int y = (int) -location.getY();

			int width = (int) puzzleView.getPuzzleWidth();
			int height = (int) puzzleView.getPuzzleHeight();
			context2d.clip();
			context2d.drawImage(image, x, y, width, height);
			context2d.restore();

		}

	}

}
