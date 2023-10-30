export interface IConnectorRepository {
  getById(id: number): void;
  delete(id: number): void;
}
