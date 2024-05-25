namespace Pacman.Web.Api.Dal.Models.Abstractions;

public interface IEntity<TKey>
    where TKey : struct, IEquatable<TKey>
{
    TKey Id { get; set; }
}
