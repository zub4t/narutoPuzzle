package mpjp.client;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import mpjp.client.MPJPResources.MPJPAudioResource;
import mpjp.shared.MPJPException;
import mpjp.shared.PieceStatus;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleLayout;
import mpjp.shared.PuzzleSelectInfo;
import mpjp.shared.geom.Point;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */

public class ASW_Trab3 implements EntryPoint {
	final TabLayoutPanel tabLayoutPanel = new TabLayoutPanel(1.5, Unit.EM);
	final RootPanel initialMenu = RootPanel.get("initial-menu");
	private int gameNum = 0;
	private final MPJPServiceAsync mpjpService = GWT.create(MPJPService.class);
	private final Logger logger = java.util.logging.Logger.getLogger("ASW-Trab 3");
	private BundleMap bundleMap = null;
	private boolean isDragging = false;
	private Paint paint = null;
	private int currentPieceSelected = -1;
	private Timer timer = null;
	private MPJPAudioResource bgMusic = MPJPResources.loadAudio("/songs/narutoMusic.mp3");
	private MPJPAudioResource connectMusic = MPJPResources.loadAudio("/songs/dravenPassiva.mp3");
	private MPJPAudioResource victoryMusic = MPJPResources.loadAudio("/songs/victoryMusic.mp3");

//	private MPJPAudioResource newGameMusic = MPJPResources.loadAudio("/songs/hinataNARUTOO.mp3");
//	private MPJPAudioResource searchGameMusic = MPJPResources.loadAudio("/songs/leeNARUTOO.mp3");

	public void onModuleLoad() {

		delay(0, () -> {
			bgMusic.play();
		});

		bundleMap = new BundleMap();
		paint = new Paint();
		paint.setBundleMap(bundleMap);
		tabLayoutPanel.add(createMenu(), "Menu");
		tabLayoutPanel.setSize("100vw", "100vh");
		tabLayoutPanel.setAnimationDuration(1000);
		RootPanel.get("tab").add(tabLayoutPanel);
		poolAnpaintaint();

	}

	/**
	 * A function to deal with mouse over and out on a button
	 * 
	 * @param gif
	 * @param target
	 */
	public void setMouseEventButton(FlowPanel gif, FlowPanel target) {
		target.addDomHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				gif.setVisible(true);
			}
		}, MouseOverEvent.getType());
		target.addDomHandler(new MouseOutHandler() {
//			@Override
			public void onMouseOut(MouseOutEvent event) {
				gif.setVisible(false);
			}
		}, MouseOutEvent.getType());
	}

	/**
	 * create the initial menu to search or create a new Game
	 * 
	 * @return
	 */
	public FlowPanel createMenu() {

		FlowPanel container = new FlowPanel();
		FlowPanel newGame = new FlowPanel();
		FlowPanel textNewGame = new FlowPanel();
		textNewGame.add(new Label("Novo Jogo"));
		textNewGame.addStyleName("text-settings");
		FlowPanel newGameGif = new FlowPanel("span");
		newGameGif.addStyleName("kyuubi-naruto");
		newGameGif.setVisible(false);
		newGame.add(textNewGame);
		newGame.add(newGameGif);

		FlowPanel search = new FlowPanel();
		FlowPanel textSearch = new FlowPanel();
		textSearch.add(new Label("Juntar-se"));
		textSearch.addStyleName("text-settings");
		FlowPanel searchGif = new FlowPanel("span");
		searchGif.addStyleName("kyuubiShippuden-naruto");
		searchGif.setVisible(false);
		search.add(textSearch);
		search.add(searchGif);

		newGame.addDomHandler(new NewGameHandler(), ClickEvent.getType());
		search.addDomHandler(new SearchGameHandler(), ClickEvent.getType());
		setMouseEventButton(newGameGif, newGame);
		setMouseEventButton(searchGif, search);

		newGame.addStyleName("button-naruto");
		search.addStyleName("button-naruto");

		DockPanel paint = new DockPanel();
		paint.add(newGame, DockPanel.NORTH);
		paint.add(search, DockPanel.NORTH);

		paint.setCellHorizontalAlignment(newGame, HasHorizontalAlignment.ALIGN_CENTER);
		paint.setCellHorizontalAlignment(search, HasHorizontalAlignment.ALIGN_CENTER);
		paint.setSize("300px", "200px");
		container.add(paint);
		container.addStyleName("initial-menu");
		return container;

	}

	/**
	 * A function to handle with mouse events inside on canvas
	 * 
	 * @param workspaceId
	 */
	void handleMouseEvents(String workspaceId) {
		Canvas canvas = bundleMap.mapCanvas.get(workspaceId);
		canvas.addMouseDownHandler(e -> {
			mpjpService.selectPiece(workspaceId, new Point(e.getClientX(), e.getClientY()),
					new AsyncCallback<Integer>() {

						@Override
						public void onSuccess(Integer pieceId) {

							if (pieceId != null) {
								currentPieceSelected = pieceId;
								isDragging = true;
							}
						}

						@Override
						public void onFailure(Throwable caught) {
							logger.log(Level.SEVERE, caught.toString());

						}
					});

		});
		canvas.addMouseMoveHandler(e -> {
			if (isDragging && currentPieceSelected != -1) {
				PuzzleLayout puzzleLayout = bundleMap.mapPuzzleLayout.get(workspaceId);
				Point pieceCenter = puzzleLayout.getPieces().get(currentPieceSelected).getPosition();
				Point point = new Point(e.getX(), e.getY());
				List<Integer> piecesInBlock = puzzleLayout.getBlocks()
						.get(puzzleLayout.getPieces().get(currentPieceSelected).getBlock());

				for (Integer piece : piecesInBlock) {
					Point translate = new Point(e.getX() - pieceCenter.getX(), e.getY() - pieceCenter.getY());
					Point pieceCenter2 = puzzleLayout.getPieces().get(piece).getPosition();
					puzzleLayout.getPieces().get(piece).setPosition(
							new Point(pieceCenter2.getX() + translate.getX(), pieceCenter2.getY() + translate.getY()));

				}
				puzzleLayout.getPieces().get(currentPieceSelected).setPosition(point);
				bundleMap.mapCanvas.get(workspaceId).getContext2d().save();
				paint.draw(workspaceId, bundleMap.mapImageElement.get(workspaceId));
				bundleMap.mapCanvas.get(workspaceId).getContext2d().restore();

			}
		});
		canvas.addMouseUpHandler(e -> {
			try {
				mpjpService.connect(workspaceId, currentPieceSelected, new Point(e.getX(), e.getY()),
						new AsyncCallback<PuzzleLayout>() {

							@Override
							public void onSuccess(PuzzleLayout result) {
								if (result.getBlocks().size() < bundleMap.getMapPuzzleLayout().get(workspaceId)
										.getBlocks().size()) {
									connectMusic.play();
								}

								bundleMap.getMapPuzzleLayout().remove(workspaceId);
								bundleMap.getMapPuzzleLayout().put(workspaceId, result);
								currentPieceSelected = -1;
								isDragging = false;
								delay(0, () -> {

									int piecesWithBlock = 0;
									for (Entry<Integer, PieceStatus> entry : result.getPieces().entrySet()) {
										if (entry.getValue().getBlock() != 0) {
											piecesWithBlock++;
										}
									}

									if (piecesWithBlock == 0) {

										int end = tabLayoutPanel.getSelectedIndex();

										if (end != 0) {
											
											DOM.getElementById("image-100").addClassName("visible");
											victoryMusic.play();
											bgMusic.pause();
											bundleMap.getMapPuzzleSelectInfo().get(workspaceId)
													.setPercentageSolved(100);
											bundleMap.getMapCanvas().get(workspaceId).setCoordinateSpaceWidth(bundleMap
													.getMapCanvas().get(workspaceId).getCoordinateSpaceWidth());
											int dInitial = 0;
											for (int i = 10; i <= 100; i = i + 10) {
												final int temp = i;
												delay(dInitial, () -> {
													bundleMap.getMapCanvas().get(workspaceId).getContext2d().drawImage(
															bundleMap.getMapImageElement().get(workspaceId), 0, 0,
															bundleMap.getMapCanvas().get(workspaceId).getOffsetWidth()
																	* (temp / 100.0),
															bundleMap.getMapCanvas().get(workspaceId).getOffsetHeight()
																	* (temp / 100.0));
												});
												dInitial += 50;
											}
										
											delay(10000, () -> {
												DOM.getElementById("image-100").removeClassName("visible");
												tabLayoutPanel.getTabWidget(end).removeFromParent();
												timer.cancel();
												bundleMap.mapCanvas.remove(workspaceId);
												bundleMap.getMapPuzzleLayout().remove(workspaceId);
												bundleMap.getMapPuzzleView().remove(workspaceId);
												bundleMap.getMapPuzzleSelectInfo().remove(workspaceId);
												bundleMap.getMapImageElement().remove(workspaceId);

												victoryMusic.pause();
												bgMusic.play();

											});
											poolAnpaintaint();
										}

									}

								});

							}

							@Override
							public void onFailure(Throwable caught) {
								logger.log(Level.SEVERE, caught.toString());

							}
						});
			} catch (MPJPException e1) {
				logger.log(Level.SEVERE, e1.toString());

			}
		});

	}

	/**
	 * A function to set up the timer for update all the canvas
	 */
	private void poolAnpaintaint() {
		timer = new Timer() {
			@Override
			public void run() {
				for (Entry<String, Canvas> entry : bundleMap.mapCanvas.entrySet()) {

					if (currentPieceSelected == -1
							&& bundleMap.getMapPuzzleSelectInfo().get(entry.getKey()).getPercentageSolved() != 100)
						paint.drawBasedOnServer(entry.getKey());
				}

			}
		};
		timer.scheduleRepeating(500);
	}

	/**
	 * @author marco A function defined to add functionality to the newGame button
	 *         this bundle of code will spawn a dialog to create a new game the
	 *         dialog box will contain fields like name, grid, image, and cuttings
	 */
	class NewGameHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			connectMusic.play();
			DialogBox dialogBox = new DialogBox();
			dialogBox.setText("Criação de um novo Jogo");
			dialogBox.setGlassEnabled(true);
			DockPanel dockPanel = new DockPanel();

			VerticalPanel labelPanel = new VerticalPanel();
			labelPanel.add(new Label("Nome"));
			labelPanel.add(new Label("Imagem"));
			labelPanel.add(new Label("Cortes"));
			labelPanel.add(new Label("Grid"));
			labelPanel.setSpacing(20);
			labelPanel.setPixelSize(250, 120);
			dockPanel.add(labelPanel, DockPanel.WEST);

			TextBox name = new TextBox();
			ListBox imageList = new ListBox();
			ListBox cuttingsList = new ListBox();
			FlowPanel gridContainer = new FlowPanel();
			IntegerBox row = new IntegerBox();
			row.setPixelSize(50, 10);
			row.getElement().setId("row");
			IntegerBox col = new IntegerBox();
			col.getElement().setId("col");
			col.setPixelSize(50, 10);
			gridContainer.add(row);
			gridContainer.add(col);
			gridContainer.addStyleName("flex space-between  ");

			VerticalPanel inputPanel = new VerticalPanel();
			inputPanel.add(name);
			FlowPanel divImageList = new FlowPanel();
			divImageList.setStyleName("select");
			divImageList.add(imageList);
			inputPanel.add(divImageList);
			FlowPanel divCuttingsList = new FlowPanel();
			divCuttingsList.setStyleName("select");
			divCuttingsList.add(cuttingsList);
			inputPanel.add(divCuttingsList);
			inputPanel.add(gridContainer);
			inputPanel.setSpacing(10);
			inputPanel.setPixelSize(200, 120);
			dockPanel.add(inputPanel, DockPanel.EAST);

			Image imagePreview = new Image();
			imagePreview.setPixelSize(250, 250);
			imagePreview.setUrl("/mpjp/resource/puzzles/obito-uchiha.jpg");
			dockPanel.add(imagePreview, DockPanel.SOUTH);

			imageList.addDomHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					imagePreview.setUrl("/mpjp/resource/puzzles/" + imageList.getSelectedItemText());

				}
			}, ChangeEvent.getType());

			Button cancel = new Button("Cancelar");
			cancel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					dialogBox.hide();

				}
			});

			Button readyToGo = new Button("NARUTO!!!");

			readyToGo.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					dialogBox.hide();
					final Canvas canvas = Canvas.createIfSupported();
					canvas.setPixelSize(Window.getClientWidth(), Window.getClientHeight());

					canvas.setCoordinateSpaceHeight(Window.getClientHeight());
					canvas.setCoordinateSpaceWidth(Window.getClientWidth());

					PuzzleInfo puzzleInfo = new PuzzleInfo(name.getText(), imageList.getSelectedItemText(),
							cuttingsList.getSelectedItemText(), Integer.parseInt(row.getText()),
							Integer.parseInt(col.getText()), Window.getClientWidth() / 2, Window.getClientHeight() / 2);

					try {
						logger.log(Level.INFO, canvas.toString());
						mpjpService.createWorkspace(puzzleInfo, new AsyncCallback<String>() {

							@Override
							public void onSuccess(String workspaceId) {
								bundleMap.mapPuzzleSelectInfo.put(workspaceId,
										new PuzzleSelectInfo(puzzleInfo, new Date(), 0));
								bundleMap.mapCanvas.put(workspaceId, canvas);
								MPJPResources.loadImageElement(
										"/puzzles/" + bundleMap.mapPuzzleSelectInfo.get(workspaceId).getImageName(),
										img -> {
											bundleMap.mapImageElement.put(workspaceId, img);
											paint.drawBasedOnServer(workspaceId);
											handleMouseEvents(workspaceId);
										});

							}

							@Override
							public void onFailure(Throwable caught) {
								logger.log(Level.SEVERE, caught.toString());

							}
						});
					} catch (MPJPException e) {
						logger.log(Level.SEVERE, e.toString());
						e.printStackTrace();
					}
					canvas.addStyleName("canvas");
					tabLayoutPanel.add(canvas, "Jogo " + (++gameNum));
					tabLayoutPanel.selectTab(tabLayoutPanel.getWidgetCount() - 1);

				}
			});
			FlowPanel buttonContainer = new FlowPanel();
			buttonContainer.addStyleName("flex space-arround multi-button ");
			cancel.removeStyleName("gwt-Button");
			readyToGo.removeStyleName("gwt-Button");

			buttonContainer.add(cancel);
			buttonContainer.add(readyToGo);

			FlowPanel dialogContent = new FlowPanel();
			dialogContent.add(dockPanel);
			dialogContent.add(buttonContainer);

			dialogBox.setWidget(dialogContent);

			mpjpService.getAvailableImages(new AsyncCallback<Set<String>>() {

				@Override
				public void onSuccess(Set<String> result) {
					for (String img : result) {
						imageList.addItem(img);
					}
					imageList.getElement().setId("img_selected");

				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert(caught.toString());

				}
			});

			mpjpService.getAvailableCuttings(new AsyncCallback<Set<String>>() {

				@Override
				public void onSuccess(Set<String> result) {
					for (String cuttings : result) {
						cuttingsList.addItem(cuttings);
					}
					cuttingsList.getElement().setId("cutting_selected");
				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert(caught.toString());

				}
			});
			dialogBox.center();
			dialogBox.show();

		}

	}

	/**
	 * @author marco A function defined to add functionality to the searchGame
	 *         button this bundle of code will spawn a dialog to create a search for
	 *         a game the dialog box will contain games that are not fully finished
	 *         yet
	 */
	class SearchGameHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			connectMusic.play();
			DialogBox dialogBox = new DialogBox();
			dialogBox.setGlassEnabled(true);

			dialogBox.setText("Games already created");
			ListBox gamesList = new ListBox();
			FlowPanel flow = new FlowPanel();

			Image imagePreview = new Image();
			imagePreview.setPixelSize(250, 250);
			imagePreview.setUrl("/mpjp/resource/puzzles/obito-uchiha.jpg");
			gamesList.addDomHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {

					imagePreview.setUrl("/mpjp/resource/puzzles/"
							+ bundleMap.getMapPuzzleSelectInfo().get(gamesList.getSelectedValue()).getImageName());

				}
			}, ChangeEvent.getType());

			FlowPanel buttonContainer = new FlowPanel();
			buttonContainer.addStyleName("flex space-arround multi-button ");

			Button cancel = new Button("Cancelar");
			cancel.removeStyleName("gwt-Button");
			Button avancar = new Button("Naruto!!!");
			avancar.removeStyleName("gwt-Button");

			buttonContainer.add(cancel);
			buttonContainer.add(avancar);

			mpjpService.getAvailableWorkspaces(new AsyncCallback<Map<String, PuzzleSelectInfo>>() {

				@Override
				public void onSuccess(Map<String, PuzzleSelectInfo> map) {

					for (Entry<String, PuzzleSelectInfo> entry : map.entrySet()) {
						PuzzleSelectInfo infoSelected = entry.getValue();
						String gameName = infoSelected.getOwner() + " " + infoSelected.getCuttingName() + " "
								+ infoSelected.getPercentageSolved() + "%";
						bundleMap.getMapPuzzleSelectInfo().put(entry.getKey(), entry.getValue());
						gamesList.addItem(gameName, entry.getKey());

					}
					cancel.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							dialogBox.hide();

						}
					});
					avancar.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							String workspaceId = gamesList.getSelectedValue();

							mpjpService.getAvailableWorkspaces(new AsyncCallback<Map<String, PuzzleSelectInfo>>() {

								@Override
								public void onSuccess(Map<String, PuzzleSelectInfo> puzzleSelectInfo) {
									final Canvas canvas = Canvas.createIfSupported();
									canvas.setPixelSize(Window.getClientWidth(), Window.getClientHeight());
									canvas.setCoordinateSpaceHeight(Window.getClientHeight());
									canvas.setCoordinateSpaceWidth(Window.getClientWidth());
									canvas.setStyleName("canvas");
									tabLayoutPanel.add(canvas, "Jogo " + (++gameNum));
									tabLayoutPanel.selectTab(tabLayoutPanel.getWidgetCount() - 1);
									bundleMap.mapPuzzleSelectInfo.put(workspaceId, puzzleSelectInfo.get(workspaceId));
									bundleMap.mapCanvas.put(workspaceId, canvas);
									MPJPResources.loadImageElement(
											"/puzzles/" + bundleMap.mapPuzzleSelectInfo.get(workspaceId).getImageName(),
											img -> {
												bundleMap.mapImageElement.put(workspaceId, img);
												paint.drawBasedOnServer(workspaceId);
												handleMouseEvents(workspaceId);
											});
									dialogBox.hide();

								}

								@Override
								public void onFailure(Throwable caught) {
									logger.log(Level.SEVERE, caught.toString());

								}
							});

						}
					});
					FlowPanel divGameList = new FlowPanel();
					divGameList.add(gamesList);
					divGameList.addStyleName("select");
					flow.add(divGameList);
					dialogBox.add(flow);
					VerticalPanel panelV = new VerticalPanel();
					FlowPanel flowB = new FlowPanel();
					flowB.add(buttonContainer);
					flow.add(imagePreview);
					gamesList.removeStyleName("gwt-ListBox");
					flow.addStyleName("flex space-between   ");
					flowB.addStyleName("flex space-arround  ");
					panelV.add(flow);
					panelV.add(flowB);
					dialogBox.add(panelV);
					dialogBox.center();
					dialogBox.show();

				}

				@Override
				public void onFailure(Throwable caught) {
					logger.log(Level.SEVERE, caught.toString());

				}
			});

		}

	}

	public static void delay(int delayMs, Command afterDelay) {
		Scheduler.get().scheduleFixedDelay(() -> {
			afterDelay.execute();
			return false;
		}, delayMs);
	}
}
