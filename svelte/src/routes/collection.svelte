<script lang="ts">
	import { signout, subscribe } from '../store/user';
	import { goto } from '$app/navigation';
	import { onMount } from 'svelte';
	import Card from '../components/card.svelte';

	type card = {
		cardId: string;
		description: string;
		imageId: string;
		name: string;
		rarity: string;
		numberOfCopies?: number;
	};
	let userId;
	subscribe((value) => {
		console.log(value);
		userId = value.name;
	});
	let userStats: { coins: number; cardPacks: number; ownedCards: card[] } = null;
	let collection: { cards: card[] } = null;
	let openedPack: { cardIdsInOpenedPack: string[] } = null;
	let errorMsg: string = null;

	onMount(async () => {
		await fetchCollection();
		await fetchUserStats();
	});

	const fetchCollection = async () => {
		try {
			let response = await fetch('/api/cards/collection_v1_000');
			if (response.status !== 200) {
				errorMsg = 'Failed connection to server. Status ' + response.status;
				return;
			}
			const payload = await response.json();
			collection = payload.data;
			errorMsg = null;
		} catch (err) {
			errorMsg = 'Failed to connect to server: ' + err;
			return;
		}
	};
	const fetchUserStats = async () => {
		try {
			const response = await fetch('/api/user-collections/' + userId);
			if (response.status === 401) {
				signout();
				await goto('/');
				return;
			}
			if (response.status === 404) {
				errorMsg = 'Error: user info not available for ' + userId;
				return;
			}
			if (response.status !== 200) {
				errorMsg = 'Failed connection to server. Status ' + response.status;
				return;
			}
			const payload = await response.json();
			userStats = payload.data;
			errorMsg = null;
		} catch (err) {
			errorMsg = 'Failed to connect to server: ' + err;
			return;
		}
	};
	const openPack = async () => {
		console.log('test');
		try {
			const response = await fetch('/api/user-collections/' + userId, {
				method: 'PATCH',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify({ command: 'OPEN_PACK' })
			});
			if (response.status === 401) {
				signout();
				await goto('/');
				return;
			}
			if (response.status === 404) {
				errorMsg = 'Error: user info not available for ' + userId;
				return;
			}
			const payload = await response.json();
			if (response.status !== 200) {
				errorMsg = 'Failure. Status ' + response.status + '. Msg: ' + payload.message;
				return;
			}
			openedPack = payload.data;
			errorMsg = null;
			await fetchUserStats();
		} catch (e) {
			errorMsg = 'Failed to connect to server: ' + e;
			return;
		}
	};
	const closePackView = () => {
		openedPack = null;
	};
</script>

<div>
	{#if errorMsg}
		<p>{errorMsg}</p>
	{/if}
	{#if !userStats}
		<p>Loading user collection...</p>
	{:else if !collection}
		<p>Loading cards...</p>
	{:else if openedPack}
		<div>
			<button on:click|preventDefault={() => closePackView()}>Close</button>
			<h1>Pack Content</h1>
			<div>
				{#each openedPack.cardIdsInOpenedPack.map( (id) => collection.cards.find((c) => c.cardId === id) ) as card}
					<Card quantity={1} {...card} />
				{/each}
			</div>
		</div>
	{:else}
		<div>
			<p>Coins: {userStats.coins} &#128176;</p>
			<p>Packs: {userStats.cardPacks} &#127752;</p>
			{#if userStats.cardPacks > 0}
				<button on:click={() => openPack()}>Open Pack</button>
			{/if}
		</div>
		<div>
			{#each collection.cards as card}
				<Card
					{...card}
					quantity={userStats.ownedCards.find((z) => z.cardId === card.cardId)?.numberOfCopies ?? 0}
				/>
			{/each}
		</div>
	{/if}
</div>
