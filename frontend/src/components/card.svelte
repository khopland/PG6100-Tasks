<script lang="ts">
	export let imageId;
	export let name;
	export let quantity;
</script>

<div>
	<h3>{name + (quantity ? ' (' + quantity + ')' : '')}</h3>
	<img src={'/api/cards/imgs/' + imageId} alt="image of monster" />
</div>
