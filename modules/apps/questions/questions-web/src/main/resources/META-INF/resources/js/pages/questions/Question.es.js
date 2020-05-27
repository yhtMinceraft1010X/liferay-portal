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

import {useMutation} from '@apollo/client';
import ClayButton from '@clayui/button';
import ClayForm from '@clayui/form';
import ClayIcon from '@clayui/icon';
import ClayLink from '@clayui/link';
import ClayNavigationBar from '@clayui/navigation-bar';
import {Editor} from 'frontend-editor-ckeditor-web';
import React, {useCallback, useContext, useEffect, useState} from 'react';
import {withRouter} from 'react-router-dom';

import {AppContext} from '../../AppContext.es';
import Answer from '../../components/Answer.es';
import ArticleBodyRenderer from '../../components/ArticleBodyRenderer.es';
import CreatorRow from '../../components/CreatorRow.es';
import Link from '../../components/Link.es';
import Modal from '../../components/Modal.es';
import PaginatedList from '../../components/PaginatedList.es';
import Rating from '../../components/Rating.es';
import RelatedQuestions from '../../components/RelatedQuestions.es';
import SectionLabel from '../../components/SectionLabel.es';
import Subscription from '../../components/Subscription.es';
import TagList from '../../components/TagList.es';
import {
	createAnswerQuery,
	deleteMessageBoardThreadQuery,
	getMessages,
	getThread,
	markAsAnswerMessageBoardMessageQuery,
} from '../../utils/client.es';
import lang from '../../utils/lang.es';
import {
	dateToBriefInternationalHuman,
	getCKEditorConfig,
	onBeforeLoadCKEditor,
} from '../../utils/utils.es';

export default withRouter(
	({
		history,
		location: key,
		match: {
			params: {questionId},
			url,
		},
	}) => {
		const context = useContext(AppContext);

		const [answers, setAnswers] = useState({});
		const [articleBody, setArticleBody] = useState();
		const [deleteModalVisible, setDeleteModalVisible] = useState(false);
		const [page, setPage] = useState(1);
		const [pageSize, setPageSize] = useState(20);
		const [question, setQuestion] = useState();
		const [sort, setSort] = useState('active');

		useEffect(() => {
			getThread(questionId, context.siteKey).then((data) => {
				setQuestion(data);

				return data;
			});
		}, [key, context.siteKey, questionId]);

		useEffect(() => {
			if (question) {
				getMessages(question.id, page, pageSize, sort).then((data) => {
					setAnswers(data);
				});
			}
		}, [question, page, pageSize, sort]);

		const [createAnswer] = useMutation(createAnswerQuery, {
			onCompleted() {
				setArticleBody('');

				return getMessages(question.id, page, pageSize, sort).then(
					(data) => {
						setAnswers(data);
					}
				);
			},
		});

		const updateMarkAsAnswer = useCallback(
			(answerId) => {
				setAnswers({
					...answers,
					items: [
						...answers.items.map((otherAnswer) => {
							otherAnswer.showAsAnswer =
								otherAnswer.id === answerId;

							return otherAnswer;
						}),
					],
				});
			},
			[answers]
		);

		const [deleteThread] = useMutation(deleteMessageBoardThreadQuery, {
			onCompleted() {
				history.goBack();
			},
		});

		const deleteAnswer = useCallback(
			(answer) => {
				setAnswers({
					...answers,
					items: [
						...answers.items.filter(
							(otherAnswer) => answer.id !== otherAnswer.id
						),
					],
				});
			},
			[answers]
		);

		const [markAsAnswerMessageBoardMessage] = useMutation(
			markAsAnswerMessageBoardMessageQuery,
			{
				onCompleted(data) {
					setAnswers({
						...answers,
						items: [
							...answers.items.map((otherAnswer) => {
								if (
									otherAnswer.id ===
									data.patchMessageBoardMessage.id
								) {
									otherAnswer.showAsAnswer =
										data.patchMessageBoardMessage.showAsAnswer;
								}

								return otherAnswer;
							}),
						],
					});
				},
			}
		);

		const answerChange = useCallback(
			(answerId) => {
				const answer = answers.items.find(
					(answer) => answer.showAsAnswer && answer.id !== answerId
				);

				if (answer) {
					markAsAnswerMessageBoardMessage({
						variables: {
							messageBoardMessageId: answer.id,
							showAsAnswer: false,
						},
					});
				}
				updateMarkAsAnswer(answerId);
			},
			[answers.items, markAsAnswerMessageBoardMessage, updateMarkAsAnswer]
		);

		return (
			<section className="c-mt-5 questions-section questions-section-single">
				<div className="questions-container">
					{question && (
						<div className="row">
							<div className="col-md-1 text-md-center">
								<Rating
									aggregateRating={question.aggregateRating}
									entityId={question.id}
									myRating={
										question.myRating &&
										question.myRating.ratingValue
									}
									type={'Thread'}
								/>
							</div>

							<div className="col-md-10">
								<div className="align-items-end flex-column-reverse flex-md-row row">
									<div className="c-mt-4 c-mt-md-0 col-md-9">
										{!!question.messageBoardSection
											.numberOfMessageBoardSections && (
											<Link
												to={`/questions/${question.messageBoardSection.title}`}
											>
												<SectionLabel
													section={
														question.messageBoardSection
													}
												/>
											</Link>
										)}

										<h1 className="c-mt-2 question-headline">
											{question.headline}
										</h1>

										<p className="c-mb-0 small text-secondary">
											{Liferay.Language.get('asked')}{' '}
											{dateToBriefInternationalHuman(
												question.dateCreated
											)}
											{' - '}
											{Liferay.Language.get(
												'active'
											)}{' '}
											{dateToBriefInternationalHuman(
												question.dateModified
											)}
											{' - '}
											{lang.sub(
												Liferay.Language.get(
													'viewed-x-times'
												),
												[question.viewCount]
											)}
										</p>
									</div>

									<div className="col-md-3 text-right">
										<ClayButton.Group
											className="questions-actions"
											spaced={true}
										>
											{question.actions.subscribe && (
												<Subscription
													onSubscription={(
														subscribed
													) =>
														setQuestion({
															...question,
															subscribed,
														})
													}
													question={question}
												/>
											)}

											{question.actions.delete && (
												<>
													<Modal
														body={Liferay.Language.get(
															'do-you-want-to-delete–this-thread'
														)}
														callback={() => {
															deleteThread({
																variables: {
																	messageBoardThreadId:
																		question.id,
																},
															});
														}}
														onClose={() =>
															setDeleteModalVisible(
																false
															)
														}
														status="warning"
														textPrimaryButton={Liferay.Language.get(
															'delete'
														)}
														title={Liferay.Language.get(
															'delete-thread'
														)}
														visible={
															deleteModalVisible
														}
													/>
													<ClayButton
														displayType="secondary"
														onClick={() =>
															setDeleteModalVisible(
																true
															)
														}
													>
														<ClayIcon symbol="trash" />
													</ClayButton>
												</>
											)}

											{question.actions.replace && (
												<Link to={`${url}/edit`}>
													<ClayButton displayType="secondary">
														{Liferay.Language.get(
															'edit'
														)}
													</ClayButton>
												</Link>
											)}
										</ClayButton.Group>
									</div>
								</div>

								<div className="c-mt-4">
									<ArticleBodyRenderer {...question} />
								</div>

								<div className="c-mt-4">
									<TagList tags={question.keywords} />
								</div>

								<div className="c-mt-4 position-relative questions-creator text-center text-md-right">
									<CreatorRow question={question} />
								</div>

								<h3 className="c-mt-4 text-secondary">
									{answers.totalCount}{' '}
									{Liferay.Language.get('answers')}
								</h3>

								{!!answers.totalCount && (
									<div className="border-bottom c-mt-3">
										<ClayNavigationBar triggerLabel="Active">
											<ClayNavigationBar.Item
												active={sort === 'active'}
											>
												<ClayLink
													className="nav-link"
													displayType="unstyled"
													onClick={() =>
														setSort('active')
													}
												>
													{Liferay.Language.get(
														'active'
													)}
												</ClayLink>
											</ClayNavigationBar.Item>

											<ClayNavigationBar.Item
												active={sort === 'oldest'}
											>
												<ClayLink
													className="nav-link"
													displayType="unstyled"
													onClick={() =>
														setSort('oldest')
													}
												>
													{Liferay.Language.get(
														'oldest'
													)}
												</ClayLink>
											</ClayNavigationBar.Item>

											<ClayNavigationBar.Item
												active={sort === 'votes'}
											>
												<ClayLink
													className="nav-link"
													displayType="unstyled"
													onClick={() =>
														setSort('votes')
													}
												>
													{Liferay.Language.get(
														'votes'
													)}
												</ClayLink>
											</ClayNavigationBar.Item>
										</ClayNavigationBar>
									</div>
								)}

								<div className="c-mt-3">
									<PaginatedList
										activeDelta={pageSize}
										activePage={page}
										changeDelta={setPageSize}
										changePage={setPage}
										data={answers}
									>
										{(answer) => (
											<Answer
												answer={answer}
												answerChange={answerChange}
												deleteAnswer={deleteAnswer}
												key={answer.id}
											/>
										)}
									</PaginatedList>
								</div>

								{question &&
									question.actions &&
									question.actions['reply-to-thread'] && (
										<div className="c-mt-5">
											<ClayForm>
												<ClayForm.Group className="form-group-sm">
													<label htmlFor="basicInput">
														{Liferay.Language.get(
															'your-answer'
														)}

														<span className="c-ml-2 reference-mark">
															<ClayIcon symbol="asterisk" />
														</span>
													</label>

													<div className="c-mt-2">
														<Editor
															config={getCKEditorConfig()}
															data={articleBody}
															onBeforeLoad={(
																editor
															) =>
																onBeforeLoadCKEditor(
																	editor,
																	context.imageBrowseURL
																)
															}
															onChange={(event) =>
																setArticleBody(
																	event.editor.getData()
																)
															}
														/>
													</div>
												</ClayForm.Group>
											</ClayForm>

											<ClayButton
												disabled={!articleBody}
												displayType="primary"
												onClick={() => {
													createAnswer({
														variables: {
															articleBody,
															messageBoardThreadId:
																question.id,
														},
													});
												}}
											>
												{Liferay.Language.get(
													'post-answer'
												)}
											</ClayButton>
										</div>
									)}
							</div>
						</div>
					)}
					{question && question.id && (
						<RelatedQuestions question={question} />
					)}
				</div>
			</section>
		);
	}
);
