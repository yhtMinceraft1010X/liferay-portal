<#if commerceCartContentTotalDisplayContext.getCommerceOrder()?? && commerceCartContentTotalDisplayContext.getCommerceOrderPrice()??>
	<#assign
		commerceOrderPrice = commerceCartContentTotalDisplayContext.getCommerceOrderPrice()

		commerceOrderSubtotal = commerceOrderPrice.getSubtotal()

		commerceOrderTotal = commerceOrderPrice.getTotal()
	/>

	<#if commerceCartContentTotalDisplayContext.getCommerceOrder()??>
		<#assign
			commerceOrder = commerceCartContentTotalDisplayContext.getCommerceOrder()
		/>

		<#if commerceOrder.getCommerceOrderItems()??>
			<#assign
				commerceOrderItems = commerceOrder.getCommerceOrderItems()
			/>

			<#list commerceOrderItems as curCommerceOrderItem>
				<div class="pb-3">
					<div class="d-inline">
						<img height="48" src="${commerceCartContentTotalDisplayContext.getCommerceOrderItemThumbnailSrc(curCommerceOrderItem)}" width="48">
					</div>

					<h2 class="d-inline">
						<strong>${curCommerceOrderItem.getName(locale)}</strong>
					</h2>
				</div>

				<#if curCommerceOrderItem.isSubscription()>
					<#assign
						cpInstance = curCommerceOrderItem.getCPInstance()
					/>

					<h3>
						<@liferay_commerce_ui["price"] CPInstanceId=cpInstance.getCPInstanceId() />

						<p>
							<@liferay_commerce_ui["product-subscription-info"]
								CPInstanceId=cpInstance.getCPInstanceId()
								showDuration=false
							/>
						</p>
					</h3>
				</#if>
			</#list>
		</#if>
	</#if>

	<div class="py-3">
		<@liferay_commerce_ui["coupon-code"] commerceOrderId=commerceOrder.getCommerceOrderId() />
	</div>

	<h1>
		<@liferay_ui["message"] key="subtotal" /> ${commerceOrderSubtotal.format(locale)}
	</h1>

	<h1>
		<strong><@liferay_ui["message"] key="total" /> ${commerceOrderTotal.format(locale)}</strong>
	</h1>
</#if>