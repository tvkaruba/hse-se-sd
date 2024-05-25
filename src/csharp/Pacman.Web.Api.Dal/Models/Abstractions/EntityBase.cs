namespace Pacman.Web.Api.Dal.Models.Abstractions;

public abstract class EntityBase<TKey> : IEntity<TKey>
    where TKey : struct, IEquatable<TKey>
{
    public TKey Id { get; set; }
}
