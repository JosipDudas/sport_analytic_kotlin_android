package com.dudas.sportanalytic.api

enum class HttpStatusCode(val value: Int) {
    Default(0),

    Continue(100),
    SwitchingProtocols(101),
    Processing(102),

    Ok(200),
    Created(201),
    Accepted(202),
    NonAuthoritativeInformation(203),
    NoContent(204),
    ResetContent(205),
    PartialContent(206),
    MultiStatus(207),
    ImUsed(226),

    MultipleChoices(300),
    HttpStatusCodeMovedPermanently(301),
    Found(302),
    SeeOther(303),
    NotModified(304),
    UseProxy(305),
    Unused(306),
    TemporaryRedirect(307),

    BadRequest(400),
    Unauthorized(401),
    PaymentRequired(402),
    Forbidden(403),
    NotFound(404),
    MethodNotAllowed(405),
    NotAcceptable(406),
    ProxyAuthenticationRequired(407),
    RequestTimeOut(408),
    Conflict(409),
    Gone(410),
    LengthRequired(411),
    PreconditionFailed(412),
    RequestEntityTooLarge(413),
    RequestUriTooLong(414),
    UnsupportedMediaType(415),
    RequestedRangeNotSatisfiable(416),
    ExpectationFailed(417),
    ImaTeapot(418),
    UnProcessableEntity(422),
    Locked(423),
    FailedDependency(424),
    UpgradeRequired(426),

    InternalServerError(500),
    NotImplemented(501),
    BadGateway(502),
    ServiceUnavailable(503),
    GatewayTimeOut(504),
    HttpVersionNotSupported(505),
    VariantAlsoNegotiates(506),
    InsufficientStorage(507),
    BandWidthLimitExceeded(509),
    NotExtended(510)
}