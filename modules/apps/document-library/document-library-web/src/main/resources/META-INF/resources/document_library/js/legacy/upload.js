/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

AUI.add(
	'document-library-upload',
	(A) => {
		var AArray = A.Array;
		var ANode = A.Node;
		var Lang = A.Lang;
		var LString = Lang.String;
		var UploaderQueue = A.Uploader.Queue;

		var isNumber = Lang.isNumber;
		var isString = Lang.isString;

		var sub = Lang.sub;

		var CSS_ACTIVE_AREA = 'active-area';

		var CSS_APP_VIEW_ENTRY = 'app-view-entry-taglib';

		var CSS_DISPLAY_DESCRIPTIVE = 'display-descriptive';

		var CSS_DISPLAY_ICON = 'display-icon';

		var CSS_ENTRIES_EMPTY = 'entries-empty';

		var CSS_ENTRY_DISPLAY_STYLE = 'entry-display-style';

		var CSS_ENTRY_LINK = CSS_ENTRY_DISPLAY_STYLE + ' a';

		var CSS_ENTRY_SELECTOR = 'entry-selector';

		var CSS_ICON = 'icon';

		var CSS_SEARCHCONTAINER = 'searchcontainer';

		var DOC = A.config.doc;

		var REGEX_AUDIO = /\.(aac|auif|bwf|flac|mp3|mp4|m4a|wav|wma)$/i;

		var REGEX_COMPRESSED = /\.(dmg|gz|tar|tgz|zip)$/i;

		var REGEX_IMAGE = /\.(bmp|gif|jpeg|jpg|png|tiff)$/i;

		var REGEX_VIDEO = /\.(avi|flv|mpe|mpg|mpeg|mov|m4v|ogg|wmv)$/i;

		var SELECTOR_DATA_FOLDER = '[data-folder="true"]';

		var SELECTOR_DATA_FOLDER_DATA_TITLE =
			'[data-folder="true"][data-title]';

		var STR_DOT = '.';

		var SELECTOR_DISPLAY_DESCRIPTIVE = STR_DOT + CSS_DISPLAY_DESCRIPTIVE;

		var SELECTOR_DISPLAY_ICON = STR_DOT + CSS_DISPLAY_ICON;

		var SELECTOR_ENTRIES_EMPTY = STR_DOT + CSS_ENTRIES_EMPTY;

		var SELECTOR_ENTRY_DISPLAY_STYLE = STR_DOT + CSS_ENTRY_DISPLAY_STYLE;

		var SELECTOR_ENTRY_LINK = STR_DOT + CSS_ENTRY_LINK;

		var SELECTOR_SEARCH_CONTAINER = STR_DOT + CSS_SEARCHCONTAINER;

		var STR_BLANK = '';

		var STR_BOUNDING_BOX = 'boundingBox';

		var STR_CONTENT_BOX = 'contentBox';

		var STR_EXTENSION_PDF = '.pdf';

		var STR_FIRST = 'first';

		var STR_FOLDER_ID = 'folderId';

		var STR_HOST = 'host';

		var STR_LABEL = 'label';

		var STR_LIST = 'list';

		var STR_NAME = 'name';

		var STR_NAVIGATION_OVERLAY_BACKGROUND = '#FFF';

		var STR_SIZE = 'size';

		var STR_SPACE = ' ';

		var STR_ICON_DEFAULT = 'document-default';

		var STR_ICON_PDF = 'document-vector';

		var STR_ICON_IMAGE = 'document-image';

		var STR_ICON_COMPRESSED = 'document-compressed';

		var STR_ICON_MULTIMEDIA = 'document-multimedia';

		var TPL_ENTRIES_CONTAINER = '<dl class="{cssClass}"></dl>';

		var TPL_ENTRY_ROW_TITLE = `<div class="autofit-row ${
			CSS_APP_VIEW_ENTRY + STR_SPACE + CSS_ENTRY_DISPLAY_STYLE
		}">
			<div class="autofit-col">
				<span class="sticker sticker-rounded sticker-document sticker-secondary file-icon-color-0">
					<span class="sticker-overlay">
						${Liferay.Util.getLexiconIconTpl(STR_ICON_DEFAULT)}
					</span>
				</span>
			</div>

			<div class="autofit-col autofit-col-expand">
				<div class="table-title">
					<a>{0}</a>
				</div>
			</div>
		</div>`;

		var TPL_ENTRY_WRAPPER =
			'<dd class="card-page-item card-page-item-asset" data-title="{title}"></dd>';

		var TPL_ERROR_NOTIFICATION = new A.Template(
			'{title}',

			'<tpl if="invalidFiles.length < 3">',

			'<ul class="mb-0 mt-2 pl-3">',
			'<tpl for="invalidFiles">',
			'<li><b>{name}</b>: {errorMessage}</li>',
			'</tpl>',
			'</ul>',

			'</tpl>'
		);

		var TPL_HIDDEN_CHECK_BOX =
			'<input class="hide ' +
			CSS_ENTRY_SELECTOR +
			'" name="{0}" type="checkbox" value="">';

		var TPL_IMAGE_THUMBNAIL =
			themeDisplay.getPathContext() + '/documents/{0}/{1}/{2}';

		var DocumentLibraryUpload = A.Component.create({
			ATTRS: {
				appViewEntryTemplates: {
					// eslint-disable-next-line @liferay/aui/no-one
					validator: A.one,
					value: {},
				},

				columnNames: {
					setter(val) {
						val.push(STR_BLANK);
						val.unshift(STR_BLANK);

						return val;
					},
					validator: Array.isArray,
					value: [],
				},

				dimensions: {
					value: {},
				},

				displayStyle: {
					validator: isString,
					value: STR_BLANK,
				},

				documentLibraryNamespace: {
					validator: isString,
					value: STR_BLANK,
				},

				entriesContainer: {
					// eslint-disable-next-line @liferay/aui/no-one
					validator: A.one,
					value: {},
				},

				folderId: {
					getter() {
						var instance = this;

						return instance.get(STR_HOST).getFolderId();
					},
					readonly: true,
					setter: Lang.toInt,
					validator: isNumber || isString,
					value: null,
				},

				maxFileSize: {
					validator(val) {
						return isNumber(val) && val > 0;
					},
					value:
						Liferay.PropsValues
							.UPLOAD_SERVLET_REQUEST_IMPL_MAX_SIZE,
				},

				redirect: {
					validator: isString,
					value: STR_BLANK,
				},

				scopeGroupId: {
					validator: isNumber,
					value: null,
				},

				uploadURL: {
					setter: '_decodeURI',
					validator: isString,
					value: STR_BLANK,
				},

				viewFileEntryURL: {
					setter: '_decodeURI',
					validator: isString,
					value: STR_BLANK,
				},
			},
			EXTENDS: A.Plugin.Base,

			NAME: 'documentlibraryupload',

			NS: 'upload',

			prototype: {
				_addFilesToQueueBottom(files) {
					var instance = this;

					var queue = instance._getUploader().queue;

					files.forEach((item) => {
						queue.addToQueueBottom(item);
					});
				},

				_attachSubscriptions(data) {
					var instance = this;

					var handles = instance._handles;

					var displayStyle = instance._getDisplayStyle();
					var uploader = instance._getUploader();

					instance._detachSubscriptions();

					if (data.folder) {
						handles.push(
							uploader.on(
								'alluploadscomplete',
								instance._showFolderUploadComplete,
								instance,
								data
							),
							uploader.on(
								'totaluploadprogress',
								instance._showFolderUploadProgress,
								instance,
								data
							),
							uploader.on(
								'uploadcomplete',
								instance._detectUploadError,
								instance,
								data
							),
							uploader.on(
								'uploadstart',
								instance._showFolderUploadStarting,
								instance,
								data
							)
						);
					}
					else {
						handles.push(
							uploader.after(
								'fileuploadstart',
								instance._showFileUploadStarting,
								instance
							),
							uploader.on(
								'uploadcomplete',
								instance._showFileUploadComplete,
								instance,
								displayStyle
							),
							uploader.on(
								'uploadprogress',
								instance._showFileUploadProgress,
								instance
							)
						);
					}
				},

				_bindDragDropUI() {
					var instance = this;
					// eslint-disable-next-line @liferay/aui/no-one
					var docElement = A.one(DOC.documentElement);

					var entriesContainer = instance._entriesContainer;

					var host = instance.get(STR_HOST);

					A.getWin()._node.onbeforeunload = A.bind(
						'_confirmUnload',
						instance
					);

					var onDataRequestHandle = Liferay.on(
						host.ns('dataRequest'),
						instance._onDataRequest,
						instance
					);

					var removeCssClassTask = A.debounce(() => {
						docElement.removeClass('upload-drop-intent');
						docElement.removeClass('upload-drop-active');
					}, 500);

					var onDragOverHandle = docElement.on(
						'dragover',
						(event) => {
							var dataTransfer = event._event.dataTransfer;

							if (dataTransfer && dataTransfer.types) {
								var dataTransferTypes =
									dataTransfer.types || [];

								if (
									AArray.indexOf(dataTransferTypes, 'Files') >
										-1 &&
									AArray.indexOf(
										dataTransferTypes,
										'text/html'
									) === -1
								) {
									event.halt();

									dataTransfer.dropEffect = 'copy';

									docElement.addClass('upload-drop-intent');

									var target = event.target;

									docElement.toggleClass(
										'upload-drop-active',
										target.compareTo(entriesContainer) ||
											entriesContainer.contains(target)
									);

									removeCssClassTask();
								}
							}
						}
					);

					var onDropHandle = docElement.delegate(
						'drop',
						(event) => {
							var dataTransfer = event._event.dataTransfer;

							if (dataTransfer) {
								var dataTransferTypes =
									dataTransfer.types || [];

								if (
									AArray.indexOf(dataTransferTypes, 'Files') >
										-1 &&
									AArray.indexOf(
										dataTransferTypes,
										'text/html'
									) === -1
								) {
									event.halt();

									var dragDropFiles = AArray(
										dataTransfer.files
									);

									event.fileList = dragDropFiles.map(
										(item) => {
											return new A.FileHTML5(item);
										}
									);

									var uploader = instance._getUploader();

									uploader.fire('fileselect', event);
								}
							}
						},
						'body, .document-container, .overlaymask, .progressbar, [data-folder="true"]'
					);

					var entriesDragDelegateHandle = entriesContainer.delegate(
						['dragleave', 'dragover'],
						(event) => {
							var dataTransfer = event._event.dataTransfer;

							var dataTransferTypes = dataTransfer.types;

							if (
								AArray.indexOf(dataTransferTypes, 'Files') >
									-1 &&
								AArray.indexOf(
									dataTransferTypes,
									'text/html'
								) === -1
							) {
								var parentElement = event.target.ancestor(
									'[data-folder="true"]'
								);

								if (!parentElement) {
									parentElement = event.target;
								}

								parentElement.toggleClass(
									CSS_ACTIVE_AREA,
									event.type === 'dragover'
								);
							}
						},
						SELECTOR_DATA_FOLDER
					);

					instance._eventHandles = [
						onDataRequestHandle,
						onDragOverHandle,
						onDropHandle,
						entriesDragDelegateHandle,
					];
				},

				_combineFileLists(fileList, queuedFiles) {
					queuedFiles.forEach((item) => {
						fileList.push(item);
					});
				},

				_confirmUnload() {
					var instance = this;

					if (instance._isUploading()) {
						return Liferay.Language.get(
							'uploads-are-in-progress-confirmation'
						);
					}
				},

				_createEntriesContainer(searchContainer, displayStyle) {
					var containerClasses =
						'list-group list-group-notification show-quick-actions-on-hover';

					if (displayStyle === CSS_ICON) {
						containerClasses = 'card-page card-page-equal-height';
					}

					var entriesContainer = ANode.create(
						Lang.sub(TPL_ENTRIES_CONTAINER, {
							cssClass: containerClasses,
						})
					);

					searchContainer
						.one('.searchcontainer-content')
						.append(entriesContainer);

					return entriesContainer;
				},

				_createEntryNode(name, size, displayStyle) {
					var instance = this;

					var entryNode;

					var entriesContainer = instance.get('entriesContainer');

					var searchContainer = entriesContainer.one(
						SELECTOR_SEARCH_CONTAINER
					);

					if (displayStyle === STR_LIST) {
						entriesContainer = searchContainer.one('tbody');

						entryNode = instance._createEntryRow(name, size);
					}
					else {
						var entriesContainerSelector =
							'dl.list-group:last-of-type';

						if (displayStyle === CSS_ICON) {
							entriesContainerSelector =
								'dl.card-page:last-of-type';

							if (
								entriesContainer
									.one(entriesContainerSelector)
									?.one('.card-type-directory')
							) {
								entriesContainerSelector = null;
							}
						}

						entriesContainer =
							entriesContainer.one(entriesContainerSelector) ||
							instance._createEntriesContainer(
								entriesContainer,
								displayStyle
							);

						var invisibleEntry =
							instance._invisibleDescriptiveEntry;

						var hiddenCheckbox = sub(TPL_HIDDEN_CHECK_BOX, [
							instance.get(STR_HOST).ns('rowIdsFileEntry'),
						]);

						if (displayStyle === CSS_ICON) {
							invisibleEntry = instance._invisibleIconEntry;
						}

						entryNode = invisibleEntry.clone();

						entryNode.append(hiddenCheckbox);

						var entryLink = entryNode.one('a');

						var entryTitle = entryLink;

						entryLink.attr('title', name);

						entryTitle.setContent(name);

						instance._hideEmptyResultsMessage(searchContainer);

						var searchContainerWrapper = document.querySelector(
							'div.lfr-search-container-wrapper'
						);

						if (searchContainerWrapper) {
							searchContainerWrapper.style.display = 'block';

							searchContainerWrapper.classList.remove('hide');
						}
					}

					entryNode.attr({
						'data-title': name,
						'id': A.guid(),
					});

					if (displayStyle === CSS_ICON) {
						var entryNodeWrapper = ANode.create(
							Lang.sub(TPL_ENTRY_WRAPPER, {
								title: name,
							})
						);

						entryNodeWrapper.append(entryNode);

						entryNode = entryNodeWrapper;
					}

					entriesContainer.append(entryNode);

					entryNode.show().scrollIntoView();

					return entryNode;
				},

				_createEntryRow(name, size) {
					var instance = this;

					var searchContainer = instance._getSearchContainer();

					var columnValues = instance._columnNames.map(
						(item, index) => {
							var value = STR_BLANK;

							if (item === STR_NAME) {
								value = sub(TPL_ENTRY_ROW_TITLE, [name]);
							}
							else if (item === STR_SIZE) {
								value = Liferay.Util.formatStorage(size);
							}
							else if (item === 'downloads') {
								value = '0';
							}
							else if (index === 0) {
								value = sub(TPL_HIDDEN_CHECK_BOX, [
									instance
										.get(STR_HOST)
										.ns('rowIdsFileEntry'),
								]);
							}

							return value;
						}
					);

					var rowid = A.guid();
					var row = searchContainer.addRow(columnValues, rowid);

					row.attr('data-draggable', true);
					row.attr('data-rowid', rowid);

					return row;
				},

				_createOverlay(target, background) {
					var instance = this;

					var displayStyle = instance._getDisplayStyle();
					var overlay = new A.OverlayMask({
						background: background || null,
						target:
							displayStyle !== CSS_ICON
								? target
								: target.one('.card'),
					}).render();

					overlay
						.get(STR_BOUNDING_BOX)
						.addClass('portlet-document-library-upload-mask');

					return overlay;
				},

				_createProgressBar(target) {
					return new A.ProgressBar({
						height: 16,
						on: {
							complete() {
								this.set(STR_LABEL, 'complete!');
							},
							valueChange(event) {
								this.set(STR_LABEL, event.newVal + '%');
							},
						},
						width: target.width() * 0.8,
					});
				},

				_createUploadStatus(target, file) {
					var instance = this;

					var overlay = instance._createOverlay(target);
					var progressBar = instance._createProgressBar(target);

					overlay.show();

					if (file) {
						file.overlay = overlay;
						file.progressBar = progressBar;
						file.target = target;
					}
					else {
						target.overlay = overlay;
						target.progressBar = progressBar;
					}
				},

				_decodeURI(val) {
					return decodeURI(val);
				},

				_destroyEntry() {
					var instance = this;

					var currentUploadData = instance._getCurrentUploadData();

					var fileList = currentUploadData.fileList;

					if (!currentUploadData.folder) {
						fileList.forEach((item) => {
							item.overlay.destroy();

							item.progressBar.destroy();
						});
					}

					AArray.invoke(fileList, 'destroy');
				},

				_detachSubscriptions() {
					var instance = this;

					var handles = instance._handles;

					AArray.invoke(handles, 'detach');

					handles.length = 0;
				},

				_detectUploadError(event, data, response) {
					var instance = this;

					data = data || instance._getCurrentUploadData();
					response =
						response || instance._getUploadResponse(event.data);

					if (response.error) {
						var file = event.file;

						var invalidFileIndex = data.fileList.findIndex(
							({name}) => file.name === name
						);

						if (invalidFileIndex !== -1) {
							var invalidFile = data.fileList[invalidFileIndex];
							invalidFile.errorMessage = response.message;

							data.fileList.splice(invalidFileIndex, 1);

							data.invalidFiles.push(invalidFile);
						}
					}
				},

				_getCurrentUploadData() {
					var instance = this;

					var dataSet = instance._getDataSet();

					return dataSet.get(STR_FIRST);
				},

				_getDataSet() {
					var instance = this;

					var dataSet = instance._dataSet;

					if (!dataSet) {
						dataSet = new A.DataSet();

						instance._dataSet = dataSet;
					}

					return dataSet;
				},

				_getDisplayStyle(style) {
					var instance = this;

					var displayStyle = instance._displayStyle;

					if (style) {
						displayStyle = style === displayStyle;
					}

					return displayStyle;
				},

				_getEmptyMessage() {
					var instance = this;

					var emptyMessage = instance._emptyMessage;

					if (!emptyMessage) {
						emptyMessage = instance._entriesContainer.one(
							SELECTOR_ENTRIES_EMPTY
						);

						instance._emptyMessage = emptyMessage;
					}

					return emptyMessage;
				},

				_getFolderEntryNode(target) {
					var folderEntry;

					var overlayContentBox = target.hasClass('overlay-content');

					if (overlayContentBox) {
						var overlay = A.Widget.getByNode(target);

						folderEntry = overlay._originalConfig.target;
					}
					else {
						if (target.attr('data-folder') === 'true') {
							folderEntry = target;
						}

						if (!folderEntry) {
							folderEntry = target.ancestor(
								SELECTOR_ENTRY_LINK + SELECTOR_DATA_FOLDER
							);
						}

						if (!folderEntry) {
							folderEntry = target.ancestor(
								SELECTOR_DATA_FOLDER_DATA_TITLE
							);
						}
					}

					return folderEntry;
				},

				_getImageThumbnail(fileName) {
					var instance = this;

					return sub(TPL_IMAGE_THUMBNAIL, [
						instance._scopeGroupId,
						instance.get(STR_FOLDER_ID),
						fileName,
					]);
				},

				_getMediaIcon(fileName) {
					var iconName = STR_ICON_DEFAULT;

					if (REGEX_IMAGE.test(fileName)) {
						iconName = STR_ICON_IMAGE;
					}
					else if (
						LString.endsWith(
							fileName.toLowerCase(),
							STR_EXTENSION_PDF
						)
					) {
						iconName = STR_ICON_PDF;
					}
					else if (
						REGEX_AUDIO.test(fileName) ||
						REGEX_VIDEO.test(fileName)
					) {
						iconName = STR_ICON_MULTIMEDIA;
					}
					else if (REGEX_COMPRESSED.test(fileName)) {
						iconName = STR_ICON_COMPRESSED;
					}

					return iconName;
				},

				_getNavigationOverlays() {
					var instance = this;

					var navigationOverlays = instance._navigationOverlays;

					if (!navigationOverlays) {
						navigationOverlays = [];

						var createNavigationOverlay = function (target) {
							if (target) {
								var overlay = instance._createOverlay(
									target,
									STR_NAVIGATION_OVERLAY_BACKGROUND
								);

								navigationOverlays.push(overlay);
							}
						};

						var entriesContainer = instance.get('entriesContainer');

						createNavigationOverlay(
							entriesContainer.one(
								'.app-view-taglib.lfr-header-row'
							)
						);
						createNavigationOverlay(
							instance.get('.searchcontainer')
						);

						instance._navigationOverlays = navigationOverlays;
					}

					return navigationOverlays;
				},

				_getSearchContainer() {
					var instance = this;

					var searchContainerNode = instance._entriesContainer.one(
						SELECTOR_SEARCH_CONTAINER
					);

					return Liferay.SearchContainer.get(
						searchContainerNode.attr('id')
					);
				},

				_getTargetFolderId(target) {
					var instance = this;

					var folderEntry = instance._getFolderEntryNode(target);

					return (
						(folderEntry &&
							Lang.toInt(folderEntry.attr('data-folder-id'))) ||
						instance.get(STR_FOLDER_ID)
					);
				},

				_getUploadResponse(responseData) {
					var instance = this;

					var error;
					var message;

					try {
						responseData = JSON.parse(responseData);
					}
					catch (error) {}

					if (Lang.isObject(responseData)) {
						error = Boolean(
							responseData.status === 0 ||
								(responseData.status &&
									responseData.status >= 490 &&
									responseData.status < 500)
						);

						if (error) {
							message = responseData.message;
						}
						else {
							message =
								instance.get(STR_HOST).ns('fileEntryId=') +
								responseData.fileEntryId;
						}
					}

					return {
						error,
						message,
					};
				},

				_getUploadStatus(key) {
					var instance = this;

					var dataSet = instance._getDataSet();

					return dataSet.item(String(key));
				},

				_getUploadURL(folderId) {
					var instance = this;

					if (!instance._uploadURL) {
						instance._uploadURL = instance._decodeURI(
							Liferay.Util.addParams(
								{
									redirect: instance.get('redirect'),
									ts: Date.now(),
								},
								instance.get('uploadURL')
							)
						);
					}

					return sub(instance._uploadURL, {
						folderId,
					});
				},

				_getUploader() {
					var instance = this;

					var uploader = instance._uploader;

					if (!uploader) {
						uploader = new A.Uploader({
							appendNewFiles: false,
							fileFieldName: 'file',
							multipleFiles: true,
							simLimit: 1,
						});

						var navigationOverlays = instance._getNavigationOverlays();

						uploader.on('uploadstart', () => {
							AArray.invoke(navigationOverlays, 'show');
						});

						uploader.after(
							'alluploadscomplete',
							instance._onAllUploadsComplete,
							instance
						);

						uploader.get(STR_BOUNDING_BOX).hide();

						uploader.render();

						uploader.after(
							'alluploadscomplete',
							instance._startNextUpload,
							instance
						);
						uploader.after(
							'fileselect',
							instance._onFileSelect,
							instance
						);

						instance._uploader = uploader;
					}

					return uploader;
				},

				_hideEmptyResultsMessage(searchContainer) {
					var id = searchContainer.getAttribute('id');

					var emptyResultsMessage = document.getElementById(
						`${id}EmptyResultsMessage`
					);

					if (emptyResultsMessage) {
						emptyResultsMessage.style.display = 'none';

						emptyResultsMessage.classList.remove('hide');
					}
				},

				_isUploading() {
					var instance = this;

					var uploader = instance._uploader;

					var queue = uploader && uploader.queue;

					return (
						!!queue &&
						(queue.queuedFiles.length > 0 ||
							queue.numberOfUploads > 0 ||
							// eslint-disable-next-line @liferay/aui/no-object
							!A.Object.isEmpty(queue.currentFiles)) &&
						queue._currentState === UploaderQueue.UPLOADING
					);
				},

				_onAllUploadsComplete() {
					var instance = this;
					var navigationOverlays = instance._getNavigationOverlays();

					AArray.invoke(navigationOverlays, 'hide');

					var currentUploadData = instance._getCurrentUploadData();

					var invalidFilesLength =
						currentUploadData.invalidFiles.length;
					var validFilesLength = currentUploadData.fileList.length;

					var searchContainer = instance._getSearchContainer();

					var emptyMessage = instance._getEmptyMessage();

					if (
						emptyMessage &&
						!emptyMessage.hasClass('hide') &&
						!validFilesLength
					) {
						emptyMessage.hide(true);
					}

					if (validFilesLength) {
						var openToastProps = {
							message: Liferay.Util.sub(
								instance._strings.xValidFilesUploaded,
								validFilesLength
							),
							toastProps: {
								className: 'alert-full',
							},
							type: 'success',
						};

						if (!currentUploadData.folder && !invalidFilesLength) {
							var reloadButtonClassName = 'dl-reload-button';

							openToastProps.message =
								openToastProps.message +
								` <button class="btn btn-sm btn-link alert-link ${reloadButtonClassName}">${instance._strings.reloadButton}</button>`;

							openToastProps.onClick = ({event}) => {
								if (
									event.target.classList.contains(
										reloadButtonClassName
									)
								) {
									Liferay.Portlet.refresh(
										`#p_p_id${instance._documentLibraryNamespace}`
									);
								}
							};
						}

						Liferay.Util.openToast(openToastProps);
					}

					if (invalidFilesLength) {
						if (!currentUploadData.folder) {
							var displayStyle = instance._getDisplayStyle();

							currentUploadData.invalidFiles.forEach(
								(invalidFile) => {
									if (invalidFile.target) {
										if (displayStyle !== STR_LIST) {
											invalidFile.target.remove();
											invalidFile.target.destroy();
										}
										else {
											searchContainer.deleteRow(
												invalidFile.target,
												invalidFile.target.getAttribute(
													'data-rowid'
												)
											);
										}
									}

									invalidFile.progressBar?.destroy();
									invalidFile.overlay?.destroy();
								}
							);

							var entriesContainer = instance.get(
								'entriesContainer'
							);

							if (
								!validFilesLength &&
								!searchContainer.getSize()
							) {
								instance._showEmptyResultsMessage(
									entriesContainer.one(
										SELECTOR_SEARCH_CONTAINER
									)
								);
							}
						}

						var message = TPL_ERROR_NOTIFICATION.parse({
							invalidFiles: currentUploadData.invalidFiles,
							title: Liferay.Util.sub(
								instance._strings.xInvalidFilesUploaded,
								invalidFilesLength
							),
						});

						Liferay.Util.openToast({
							message,
							toastProps: {
								className: 'alert-full',
							},
							type: 'danger',
						});
					}
				},

				_onDataRequest(event) {
					var instance = this;

					if (instance._isUploading()) {
						event.halt();
					}
				},

				_onFileSelect(event) {
					var instance = this;

					var target = event.details[0].target;

					var filesPartition = instance._validateFiles(
						event.fileList
					);

					instance._updateStatusUI(target, filesPartition);

					instance._queueSelectedFiles(target, filesPartition);
				},

				_positionProgressBar(overlay, progressBar) {
					var progressBarBoundingBox = progressBar.get(
						STR_BOUNDING_BOX
					);

					progressBar.render(overlay.get(STR_BOUNDING_BOX));

					progressBarBoundingBox.center(overlay.get(STR_CONTENT_BOX));
				},

				_queueSelectedFiles(target, filesPartition) {
					var instance = this;

					var key = instance._getTargetFolderId(target);

					var keyData = instance._getUploadStatus(key);

					var validFiles = filesPartition.matches;

					if (keyData) {
						instance._updateDataSetEntry(key, keyData, validFiles);
					}
					else {
						var dataSet = instance._getDataSet();

						var folderNode = null;

						var folder = key !== instance.get(STR_FOLDER_ID);

						if (folder) {
							folderNode = instance._getFolderEntryNode(target);
						}

						dataSet.add(key, {
							fileList: validFiles,
							folder,
							folderId: key,
							invalidFiles: filesPartition.rejects,
							target: folderNode,
						});
					}

					if (!instance._isUploading()) {
						instance._startUpload();
					}
				},

				_showEmptyResultsMessage(searchContainer) {
					var id = searchContainer.getAttribute('id');

					var emptyResultsMessage = document.getElementById(
						`${id}EmptyResultsMessage`
					);

					if (emptyResultsMessage) {
						emptyResultsMessage.style.display = 'block';

						emptyResultsMessage.classList.remove('hide');
					}
				},

				_showFileUploadComplete(event, displayStyle) {
					var instance = this;

					var file = event.file;

					var fileNode = file.target;

					var response = instance._getUploadResponse(event.data);

					if (response) {
						var hasErrors = !!response.error;

						if (hasErrors) {
							instance._detectUploadError(event, null, response);
						}
						else {
							var fileEntryId = JSON.parse(event.data)
								.fileEntryId;

							instance._updateEntryUI(
								fileNode,
								file.name,
								displayStyle
							);

							instance._updateFileLink(
								fileNode,
								response.message,
								displayStyle
							);

							instance._updateFileHiddenInput(
								fileNode,
								fileEntryId
							);
						}
					}

					file.overlay.hide();
				},

				_showFileUploadProgress(event) {
					var instance = this;

					instance._updateProgress(
						event.file.progressBar,
						event.percentLoaded
					);
				},

				_showFileUploadStarting(event) {
					var instance = this;

					var file = event.file;

					instance._positionProgressBar(
						file.overlay,
						file.progressBar
					);
				},

				_showFolderUploadComplete(_event, uploadData) {
					var folderEntry = uploadData.target;

					folderEntry.overlay.hide();
				},

				_showFolderUploadProgress(event, uploadData) {
					var instance = this;

					instance._updateProgress(
						uploadData.target.progressBar,
						event.percentLoaded
					);
				},

				_showFolderUploadStarting(_event, uploadData) {
					var instance = this;

					var target = uploadData.target;

					instance._positionProgressBar(
						target.overlay,
						target.progressBar
					);
				},

				_startNextUpload() {
					var instance = this;

					instance._destroyEntry();

					var dataSet = instance._getDataSet();

					dataSet.removeAt(0);

					if (dataSet.length) {
						instance._startUpload();
					}
				},

				_startUpload() {
					var instance = this;

					var uploadData = instance._getCurrentUploadData();

					var fileList = uploadData.fileList;

					var uploader = instance._getUploader();

					if (fileList.length) {
						var uploadURL = instance._getUploadURL(
							uploadData.folderId
						);

						instance._attachSubscriptions(uploadData);

						uploader.uploadThese(fileList, uploadURL);
					}
					else {
						uploader.fire('alluploadscomplete');
					}
				},

				_updateDataSetEntry(key, data, unmergedData) {
					var instance = this;

					var currentUploadData = instance._getCurrentUploadData();

					if (currentUploadData.folderId === key) {
						instance._addFilesToQueueBottom(unmergedData);
					}
					else {
						instance._combineFileLists(data.fileList, unmergedData);

						var dataSet = instance._getDataSet();

						dataSet.replace(key, data);
					}
				},

				_updateEntryIcon(node, fileName) {
					var instance = this;

					var stickerNode = node.one('.sticker-overlay');
					var mediaIcon = instance._getMediaIcon(fileName);

					stickerNode.html(Liferay.Util.getLexiconIconTpl(mediaIcon));
				},

				_updateEntryUI(node, fileName, displayStyle) {
					var instance = this;

					var displayStyleList = displayStyle === STR_LIST;

					instance._updateEntryIcon(node, fileName);

					if (!displayStyleList && REGEX_IMAGE.test(fileName)) {
						instance._updateThumbnail(node, fileName, displayStyle);
					}
				},

				_updateFileHiddenInput(node, id) {
					var inputNode = node.one('input');

					if (inputNode) {
						inputNode.val(id);
					}
				},

				_updateFileLink(node, id, displayStyle) {
					var instance = this;

					var selector = 'a';

					if (displayStyle === CSS_ICON) {
						selector = SELECTOR_ENTRY_LINK;
					}

					var link = node.all(selector);

					if (link.size()) {
						link.attr(
							'href',
							Liferay.Util.addParams(
								id,
								instance.get('viewFileEntryURL')
							)
						);
					}
				},

				_updateProgress(progressBar, value) {
					progressBar.set('value', Math.ceil(value));
				},

				_updateStatusUI(target, filesPartition) {
					var instance = this;

					var folderId = instance._getTargetFolderId(target);

					var folder = folderId !== instance.get(STR_FOLDER_ID);

					if (folder) {
						var folderEntryNode = instance._getFolderEntryNode(
							target
						);

						var folderEntryNodeOverlay = folderEntryNode.overlay;

						if (folderEntryNodeOverlay) {
							instance._updateProgress(
								folderEntryNode.progressBar,
								0
							);

							folderEntryNodeOverlay.show();
						}
						else {
							instance._createUploadStatus(folderEntryNode);
						}

						folderEntryNode.removeClass(CSS_ACTIVE_AREA);
					}
					else {
						var displayStyle = instance._getDisplayStyle();

						filesPartition.matches.map((file) => {
							var entryNode = instance._createEntryNode(
								file.name,
								file.size,
								displayStyle
							);

							instance._createUploadStatus(entryNode, file);
						});
					}
				},

				_updateThumbnail(node, fileName, displayStyle) {
					var instance = this;

					var imageNode = node.one('img');
					var thumbnailPath = instance._getImageThumbnail(fileName);

					if (!imageNode) {
						var targetNodeSelector = '.sticker-overlay svg';
						var imageClassName = 'sticker-img';

						if (displayStyle === CSS_ICON) {
							targetNodeSelector = '.card-type-asset-icon';
							imageClassName =
								'aspect-ratio-item-center-middle aspect-ratio-item-fluid';
						}

						imageNode = A.Node.create(
							`<img alt="" class="${imageClassName}" src="${thumbnailPath}" />`
						);

						var targetNode = node.one(targetNodeSelector);

						targetNode
							.get('parentNode')
							.replaceChild(imageNode, targetNode);
					}
					else {
						imageNode.attr('src', thumbnailPath);
					}
				},

				_validateFiles(data) {
					var instance = this;

					var maxFileSize = instance._maxFileSize;

					return AArray.partition(data, (item) => {
						var errorMessage;

						var size = item.get(STR_SIZE) || 0;

						var strings = instance._strings;

						if (maxFileSize !== 0 && size > maxFileSize) {
							errorMessage = sub(strings.invalidFileSize, [
								Liferay.Util.formatStorage(
									instance._maxFileSize
								),
							]);
						}
						else if (size === 0) {
							errorMessage = strings.zeroByteFile;
						}

						item.errorMessage = errorMessage;
						item.size = size;
						item.name = item.get(STR_NAME);

						return !errorMessage;
					});
				},

				destructor() {
					var instance = this;

					if (instance._dataSet) {
						instance._dataSet.destroy();
					}

					if (instance._navigationOverlays) {
						AArray.invoke(instance._navigationOverlays, 'destroy');
					}

					if (instance._uploader) {
						instance._uploader.destroy();
					}

					instance._detachSubscriptions();

					new A.EventHandle(instance._eventHandles).detach();
				},

				initializer() {
					var instance = this;

					var appViewEntryTemplates = instance.get(
						'appViewEntryTemplates'
					);

					instance._columnNames = instance.get('columnNames');
					instance._dimensions = instance.get('dimensions');
					instance._displayStyle = instance.get('displayStyle');
					instance._documentLibraryNamespace = instance.get(
						'documentLibraryNamespace'
					);
					instance._entriesContainer = instance.get(
						'entriesContainer'
					);
					instance._maxFileSize = instance.get('maxFileSize');
					instance._scopeGroupId = instance.get('scopeGroupId');

					instance._handles = [];

					instance._invisibleDescriptiveEntry = appViewEntryTemplates.one(
						SELECTOR_ENTRY_DISPLAY_STYLE +
							SELECTOR_DISPLAY_DESCRIPTIVE
					);
					instance._invisibleIconEntry = appViewEntryTemplates.one(
						SELECTOR_ENTRY_DISPLAY_STYLE + SELECTOR_DISPLAY_ICON
					);

					instance._strings = {
						invalidFileSize: Liferay.Language.get(
							'please-enter-a-file-with-a-valid-file-size-no-larger-than-x'
						),
						reloadButton: Liferay.Language.get('reload'),
						xInvalidFilesUploaded: Liferay.Language.get(
							'x-files-could-not-be-uploaded'
						),
						xValidFilesUploaded: Liferay.Language.get(
							'x-files-were-uploaded'
						),
						zeroByteFile: Liferay.Language.get(
							'the-file-contains-no-data-and-cannot-be-uploaded.-please-use-the-classic-uploader'
						),
					};

					instance._bindDragDropUI();
				},
			},
		});

		Liferay.DocumentLibraryUpload = DocumentLibraryUpload;
	},
	'',
	{
		requires: [
			'aui-component',
			'aui-data-set-deprecated',
			'aui-overlay-manager-deprecated',
			'aui-overlay-mask-deprecated',
			'aui-parse-content',
			'aui-progressbar',
			'aui-template-deprecated',
			'liferay-search-container',
			'querystring-parse-simple',
			'uploader',
		],
	}
);
